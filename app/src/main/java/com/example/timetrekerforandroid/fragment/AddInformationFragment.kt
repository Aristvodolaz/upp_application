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
class AddInformationFragment(private var syryo: Boolean) : Fragment(), AddInformationView , ScannerController.ScannerCallback, ApproveShkDialog.OnSendNewShk{

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
        binding.notStandrtBtn.setOnClickListener {
            binding.nestandartVlozhLiner.visibility = View.VISIBLE
            isNonStandard = true
        }

        binding.nameShk.text = "Шк товара: ${SPHelper.getShkWork()}"
        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameStuffWork()
        binding.size.text = "Количество товара: ${SPHelper.getItogZakaza()}"

        if(syryo) {
            binding.nameShk.visibility = View.GONE
            binding.line.visibility = View.GONE
            binding.scanLine.visibility = View.VISIBLE
        } else{
            binding.scanLine.visibility = View.GONE
            binding.line.visibility = View.VISIBLE
        }


        binding.btnDone.setOnClickListener {
//            handleDoneButtonClick()
            sendDataToService()
        }

        binding.btn.setOnClickListener {
//            handleAdditionalButtonClick()
            showDialog()
            presenter.createDuplicate(
                binding.mestoDopEt.text.toString(),
                binding.vlozhennostDopEt.text.toString(),
                binding.paletDopEt.text.toString()
            )
        }
    }

    private fun showDialog() {
        waitDialog = WaitDialog.newInstance()
        waitDialog.isCancelable = false
        fragmentManager?.let { waitDialog.show(it, WaitDialog.TAG) }
    }

    private fun sendDataToService() {
        showDialog()
        presenter.sendFinishedInformation(
            binding.mestoEt.text.toString(),
            binding.vlozhennostEt.text.toString(),
            binding.paletEt.text.toString()
        )
    }

    private fun showErrorToast() {
        Toast.makeText(
            context,
            "Указано неверное значение, проверьте количество!",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun msgSuccess(msg: String) {
        waitDialog.dismiss()
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }

    override fun msgSuccessDuplicate(msg: String) {
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        binding.mestoDopEt.text.clear()
        binding.paletDopEt.text.clear()
        binding.vlozhennostDopEt.text.clear()
        binding.nestandartVlozhLiner.visibility = View.GONE
    }

    override fun errorMessage(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun success() {
        binding.scanLine.visibility = View.GONE
        binding.line.visibility = View.VISIBLE
        Toast.makeText(context, "Штрих код добавлен!", Toast.LENGTH_SHORT).show()

    }

    override fun createNewShk(shk: String) {
        binding.scanLine.visibility = View.GONE
        binding.line.visibility = View.VISIBLE
        Log.d(" NENNENENENE", "DJKDJKFJKL")
        val dialog = ApproveShkDialog.newInstance(shk, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, "lol") }
    }

    override fun msgError(msg: String) {
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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
        _binding = null // Установите binding в null, чтобы избежать утечек памяти
    }
    override fun onDataReceived(barcodeData: String) {
        SPHelper.setShkWork(barcodeData)
        presenter.findInExcel(barcodeData, SPHelper.getNameTask())
        Log.d("BARCODE", barcodeData)
    }

    override fun onScanFailed(errorMessage: String?) {
        TODO("Not yet implemented")
    }

    override fun sendNewShk(shk: String?) {
        if (shk != null) {
            presenter.updateShk(shk)
        }
    }
}
