package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.databinding.AddInformationFragmentBinding
import com.example.timetrekerforandroid.dialog.ApproveShkDialog
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.presenter.AddInformationPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.AddInformationView

class AddInformationFragment(private var syryo: Boolean) : Fragment(), AddInformationView, ScannerController.ScannerCallback, ApproveShkDialog.OnSendNewShk {

    private var _binding: AddInformationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: AddInformationPresenter
    private var isNonStandard: Boolean = false
    private lateinit var waitDialog: WaitDialog
    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }

    companion object {
        fun newInstance(syryo: Boolean): AddInformationFragment = AddInformationFragment(syryo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AddInformationFragmentBinding.inflate(inflater, container, false)
        presenter = AddInformationPresenter(this)
        initViews()
        return binding.root
    }

    private fun initViews() {
        with(binding) {
            notStandrtBtn.setOnClickListener {
                nestandartVlozhLiner.visibility = View.VISIBLE
                isNonStandard = true
            }

            setupInitialUI()

            btnDone.setOnClickListener {
                sendDataToService()
            }

            btn.setOnClickListener {
                handleDuplicateRequest()
            }
        }
    }

    private fun AddInformationFragmentBinding.setupInitialUI() {
        nameShk.text = "Шк товара: ${SPHelper.getShkWork()}"
        nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        nameStuff.text = SPHelper.getNameStuffWork()
        size.text = "Количество товара: ${SPHelper.getItogZakaza()}"

        if (syryo) {
            nameShk.visibility = View.GONE
            line.visibility = View.GONE
            scanLine.visibility = View.VISIBLE
        } else {
            scanLine.visibility = View.GONE
            line.visibility = View.VISIBLE
        }
    }

    private fun handleDuplicateRequest() {
        if (areFieldsValidDop()) {
            showDialog()
            presenter.createDuplicate(
                binding.mestoDopEt.text.toString(),
                binding.vlozhennostDopEt.text.toString(),
                binding.paletDopEt.text.toString()
            )
        } else {
            showErrorToast("Все поля должны быть заполнены!")
        }
    }

    private fun sendDataToService() {
        if (areFieldsValid()) {
            showDialog()
            presenter.sendFinishedInformation(
                binding.mestoEt.text.toString(),
                binding.vlozhennostEt.text.toString(),
                binding.paletEt.text.toString()
            )
        } else {
            showErrorToast("Все поля должны быть заполнены!")
        }
    }

    private fun areFieldsValid(): Boolean {
        with(binding) {
            return mestoEt.text.isNotEmpty() && vlozhennostEt.text.isNotEmpty() && paletEt.text.isNotEmpty()
        }
    }

    private fun areFieldsValidDop(): Boolean {
        with(binding) {
            return mestoDopEt.text.isNotEmpty() && vlozhennostDopEt.text.isNotEmpty() && paletDopEt.text.isNotEmpty()
        }
    }

    private fun showDialog() {
        waitDialog = WaitDialog.newInstance()
        waitDialog.isCancelable = false
        fragmentManager?.let { waitDialog.show(it, WaitDialog.TAG) }
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun msgSuccess(msg: String) {
        waitDialog.dismiss()
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }

    override fun msgSuccessDuplicate(msg: String) {
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        clearDuplicateFields()
    }

    private fun clearDuplicateFields() {
        with(binding) {
            mestoDopEt.text.clear()
            paletDopEt.text.clear()
            vlozhennostDopEt.text.clear()
            nestandartVlozhLiner.visibility = View.GONE
        }
    }

    override fun errorMessage(msg: String) {
        waitDialog.dismiss()
        showErrorToast(msg)
    }

    override fun success() {
        binding.scanLine.visibility = View.GONE
        binding.line.visibility = View.VISIBLE
        showErrorToast("Штрих код добавлен!")
    }

    override fun createNewShk(shk: String) {
        binding.scanLine.visibility = View.GONE
        binding.line.visibility = View.VISIBLE
        Log.d("BARCODE_RECEIVED", shk)
        val dialog = ApproveShkDialog.newInstance(shk, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, "ApproveShkDialog") }
    }

    override fun msgError(msg: String) {
        waitDialog.dismiss()
        showErrorToast(msg)
    }

    override fun onResume() {
        super.onResume()
        resumeScanner()
    }

    override fun onPause() {
        super.onPause()
        releaseScanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releaseScanner()
        _binding = null
    }

    private fun resumeScanner() {
        try {
            scannerController.resumeScanner()
            Log.d("AddInformationFragment", "Scanner resumed")
        } catch (e: Exception) {
            Log.e("AddInformationFragment", "Error resuming scanner: ${e.message}")
            showErrorToast("Ошибка при возобновлении сканера: ${e.message}")
        }
    }

    private fun releaseScanner() {
        try {
            scannerController.releaseScanner()
            Log.d("AddInformationFragment", "Scanner released")
        } catch (e: Exception) {
            Log.e("AddInformationFragment", "Error releasing scanner: ${e.message}")
            showErrorToast("Ошибка при отключении сканера: ${e.message}")
        }
    }

    override fun onDataReceived(barcodeData: String) {
        SPHelper.setShkWork(barcodeData)
        presenter.findInExcel(barcodeData, SPHelper.getNameTask())
        Log.d("BARCODE", barcodeData)
    }

    override fun onScanFailed(errorMessage: String?) {
        Log.e("SCAN_FAILED", errorMessage ?: "Unknown error")
        showErrorToast("Ошибка сканирования: ${errorMessage ?: "Unknown error"}")
    }

    override fun sendNewShk(shk: String?) {
        shk?.let {
            presenter.updateShk(it)
        }
    }
}
