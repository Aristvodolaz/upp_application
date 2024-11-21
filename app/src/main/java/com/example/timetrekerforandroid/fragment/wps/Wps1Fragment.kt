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
import com.example.timetrekerforandroid.dialog.CancelReasonDialog
import com.example.timetrekerforandroid.factory.TaskWpsModelFactory
import com.example.timetrekerforandroid.factory.WpsModelFactory
import com.example.timetrekerforandroid.fragment.navigation.PakingFragment
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.model.TaskModel
import com.example.timetrekerforandroid.model.TaskWpsModel
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.viewModel.TaskWpsViewModel
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class Wps1Fragment : Fragment(), ScannerController.ScannerCallback, CancelReasonDialog.OnCancelReasonSelected {

    private var _binding: WpsOnseFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: WpsAdapter? = null
    private lateinit var wpsModel: WpsViewModel
    private lateinit var taskModel: TaskWpsViewModel
    private lateinit var mWaitDialog: WaitDialog

    private var isEmergencyCancel: Boolean = false
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

        taskModel = ViewModelProvider(
            this,
            TaskWpsModelFactory(TaskWpsModel(ServiceViewModule.getService()))
        )[TaskWpsViewModel::class.java]

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

        taskModel.cancelTaskResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess { handleCancelTaskResult() }
                .onFailure { error -> showToast(error.message ?: "Неизвестная ошибка") }
        }


        taskModel.endStatusResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Log.d("Wps1Fragment", "sendEndStatus завершился успешно.")
                navigateToPackingFragment()
            }.onFailure { error ->
                Log.e("Wps1Fragment", "Ошибка в sendEndStatus: ${error.message}")
                showToast(error.message ?: "Неизвестная ошибка при завершении задачи.")
            }
        }


    }
    private fun setupListeners() {
        binding.btnDone.setOnClickListener {
            wpsModel.endStatus()
        }
        binding.btnClanced.setOnClickListener {
            val dialog = CancelReasonDialog.newInstance(this@Wps1Fragment)
            dialog.isCancelable = true
            dialog.show(requireActivity().supportFragmentManager, "cancel_reason")
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


    // Обработка завершения отмены задачи
    private fun handleCancelTaskResult() {
        if (isEmergencyCancel) {
            isEmergencyCancel = false
            taskModel.sendEndStatus()
        } else {
            showToast("Ваш комментарий записан, вы можете продолжить выкладку.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToFragment(fragment: Fragment, addToBackStack: Boolean) {
        (activity as StartActivity).replaceFragment(fragment, addToBackStack)
    }

    private fun navigateToTasksFragment() {
        navigateToFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }
    private fun navigateToPackingFragment() {
        navigateToFragment(PakingFragment.newInstance(), false)
    }

    override fun onDataReceived(barcodeData: String?) {
        barcodeData?.let {
            SPHelper.setShkWork(it)
            wpsModel.checkWps(it)
        }
    }

    override fun onScanFailed(errorMessage: String?) {
        showToast(errorMessage ?: "Сканирование не удалось")
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

    override fun onReasonSelected(reason: String, comment: String) {
        isEmergencyCancel = reason == "Убрать из обработки(экстренно)"
        taskModel.cancelTask(reason, comment)
    }
}
