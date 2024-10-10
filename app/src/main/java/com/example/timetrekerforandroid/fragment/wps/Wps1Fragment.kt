package com.example.timetrekerforandroid.fragment.wps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.WpsAdapter
import com.example.timetrekerforandroid.databinding.WpsOnseFragmentBinding
import com.example.timetrekerforandroid.factory.WpsModelFactory
import com.example.timetrekerforandroid.fragment.navigation.PakingFragment
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class Wps1Fragment : Fragment(), ScannerController.ScannerCallback {

    private var _binding: WpsOnseFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: WpsAdapter? = null
    private lateinit var wpsModel: WpsViewModel

    private val scannerController by lazy { (requireActivity() as StartActivity).getScannerController() }

    companion object {
        fun newInstance(): Wps1Fragment {
            return Wps1Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WpsOnseFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        wpsModel = ViewModelProvider(
            this,
            WpsModelFactory(WpsModel(ServiceViewModule.getService()))
        )[WpsViewModel::class.java]

        setupRecyclerView()
        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(context)
    }

    private fun setupObservers() {
        wpsModel.getListZapis()
        wpsModel.zapisData.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                if (adapter == null) {
                    adapter = WpsAdapter(requireContext(), response)
                    binding.rv.adapter = adapter
                    Log.d("Wps1Fragment", "Данные получены: ${response.size}")
                    binding.coroba.text = "Всего коробов: ${adapter?.itemCount}"
                } else {
                    adapter?.updateData(response)
                    binding.coroba.text = "Всего коробов: ${adapter?.itemCount}"
                }
            }.onFailure {
                binding.coroba.visibility = View.GONE
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        }

        wpsModel.endData.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                if (response.success) {
                    (activity as StartActivity).replaceFragment(PakingFragment.newInstance(), true)
                } else {
                    Toast.makeText(context, "Записи не найдены", Toast.LENGTH_SHORT).show()
                }
            }.onFailure {
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        }

        wpsModel.checkShk.observe(viewLifecycleOwner){ result ->
            result.onSuccess { response ->
                if(response.isSuccess){
                    (activity as StartActivity).replaceFragment(Wps2Fragment.newInstance(), false)
                } else Toast.makeText(context, "Данный ШК ВПС уже используется в заказе!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setupListeners() {
        binding.btnDone.setOnClickListener {
            wpsModel.endStatus()
        }

        binding.btnAdd.setOnClickListener {
            binding.scanBox.visibility = View.VISIBLE
            binding.visableBtn.visibility = View.GONE
            // TODO: Show prompt to scan WPS barcode
        }

        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameStuffWork()
        binding.nameShk.text = "Итог заказа: ${SPHelper.getSizeTovara()}"
    }

    override fun onDataReceived(barcodeData: String?) {
        barcodeData?.let {
            SPHelper.setShkWork(it)
            wpsModel.checkWps(it)
        }
    }

    override fun onScanFailed(errorMessage: String?) {
        Toast.makeText(context, errorMessage ?: "Сканирование не удалось", Toast.LENGTH_SHORT).show()
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
        scannerController.releaseScanner()
        _binding = null
    }
}