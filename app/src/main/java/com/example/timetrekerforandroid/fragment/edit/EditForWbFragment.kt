package com.example.timetrekerforandroid.fragment.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.databinding.EditForWbFragmentBinding
import com.example.timetrekerforandroid.factory.WpsModelFactory
import com.example.timetrekerforandroid.fragment.navigation.EditFragment
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.network.response.DataWBResponse
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.viewModel.WpsViewModel
import kotlinx.coroutines.launch

class EditForWbFragment(private val data: DataWBResponse) : Fragment(), ScannerController.ScannerCallback {

    // View binding with lazy initialization
    private var _binding: EditForWbFragmentBinding? = null
    private val binding get() = _binding!!

    // ViewModel shared with the Activity
    private lateinit var wpsModel: WpsViewModel

    // ScannerController initialized lazily
    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }

    companion object {
        fun newInstance(data: DataWBResponse): EditForWbFragment {
            return EditForWbFragment(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = EditForWbFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        // Initialize ViewModel
        wpsModel = ViewModelProvider(this, WpsModelFactory(WpsModel(ServiceViewModule.getService()))).get(WpsViewModel::class.java)

        // Set initial data from received object
        binding.vlozhennostEt.setText(data.kolvo.toString())
        SPHelper.setArticuleWork(data.artikul.toString())

        binding.btnChange.setOnClickListener {
            binding.scanBox.visibility = View.VISIBLE
            binding.vlozh.visibility = View.GONE
            binding.btnChange.visibility = View.GONE
            binding.btnDone.visibility = View.GONE
        }

        binding.btnDone.setOnClickListener {
            updateWpsData(data.pallet, data.shk)
            binding.scanBox.visibility = View.GONE
            binding.vlozh.visibility = View.VISIBLE
            binding.btnChange.visibility = View.VISIBLE
            binding.btnDone.visibility = View.GONE
        }

        // Observe LiveData using coroutines for more readable asynchronous processing
        wpsModel.addData.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                // Successful data processing, navigate back to EditFragment
                navigateToEditFragment()
            }.onFailure { error ->
                // Show error message using a function for DRY
                showToast(error.message ?: "Неизвестная ошибка")
            }
        }
    }

    private fun updateWpsData(pallet: String, shk: String) {
        val vlozhennost = binding.vlozhennostEt.text.toString().toIntOrNull()
        if (vlozhennost != null) {
            lifecycleScope.launch {
                try {
                    wpsModel.updateWps(vlozhennost, pallet, shk)
                } catch (e: Exception) {
                    Log.e("EditForWbFragment", "Error updating WPS data: ${e.message}", e)
                    showToast("Ошибка при обновлении данных: ${e.message}")
                }
            }
        } else {
            showToast("Некорректное значение вложенности")
        }
    }

    private fun navigateToEditFragment() {
        (activity as? StartActivity)?.replaceFragment(EditFragment.newInstance(), false)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDataReceived(barcodeData: String?) {
        if (barcodeData.isNullOrEmpty()) {
            showToast("Штрих-код не распознан")
            return
        }
        updateWpsData(barcodeData, data.shk)
    }

    override fun onScanFailed(errorMessage: String?) {
        showToast(errorMessage ?: "Ошибка сканирования")
    }

    override fun onResume() {
        super.onResume()
        scannerController.resumeScanner()
    }

    override fun onPause() {
        super.onPause()
        scannerController.releaseScanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Properly release resources
        _binding = null
    }
}
