package com.example.timetrekerforandroid.fragment.wps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

class Wps1Fragment: Fragment(), ScannerController.ScannerCallback {

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
    ): View? {
        _binding = WpsOnseFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        wpsModel = ViewModelProvider(
            this,
            WpsModelFactory(WpsModel(ServiceViewModule.getService()))
        )[WpsViewModel::class.java]

        wpsModel.getListZapis()
        wpsModel.zapisData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                if (adapter == null) {
                    adapter = context?.let { WpsAdapter(it, response) }
                    binding.rv.layoutManager = LinearLayoutManager(context)
                    binding.rv.adapter = adapter
                    // Логирование размера списка данных
                    Log.d("Wps1Fragment", "Данные получены: ${response.size}")
                } else {
                    adapter?.updateData(response)
                }
            }.onFailure { error ->
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })


        binding.btnDone.setOnClickListener{
          wpsModel.endStatus()
        }

        wpsModel.endData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                if (response.success) {
                    (activity as StartActivity).replaceFragment(PakingFragment.newInstance(), true)
                } else {
                    Toast.makeText(context, "Записи не найдены", Toast.LENGTH_SHORT).show()
                }
            }.onFailure { error ->
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })


        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameStuffWork()
        binding.nameShk.text = "Итог заказа: ${SPHelper.getSizeTovara()}"

        binding.btnAdd.setOnClickListener{

            binding.scanBox.visibility = View.VISIBLE
            binding.visableBtn.visibility = View.GONE
        //todo отобразить что нужно отсканировать shk впс
        }
    }

    override fun onDataReceived(barcodeData: String?) {
        SPHelper.setShkWork(barcodeData)
        (activity as StartActivity).replaceFragment(Wps2Fragment.newInstance(), false)
    }

    override fun onScanFailed(errorMessage: String?) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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