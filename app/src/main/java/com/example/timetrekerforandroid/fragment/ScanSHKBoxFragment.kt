package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.databinding.ScanShkBoxFragmentBinding
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.presenter.ScanSHKBoxPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController.ScannerCallback
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.ScanSHKBoxView

class ScanSHKBoxFragment: Fragment(), ScanSHKBoxView , ScannerCallback {
    private var _binding: ScanShkBoxFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ScanSHKBoxPresenter
    private lateinit var mWaitDialog: WaitDialog

    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }
    companion object {
        fun newInstance(): ScanSHKBoxFragment {
            return ScanSHKBoxFragment()
        }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ScanShkBoxFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        presenter = ScanSHKBoxPresenter(this)
    }


    override fun onDataReceived(barcodeData: String) {
        requireActivity().runOnUiThread {
            showDialog()
            presenter.setShkBox(barcodeData)
            Log.d("BARCODE", barcodeData)
        }
    }

    override fun onScanFailed(errorMessage: String?) {
        TODO("Not yet implemented")
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

    override fun successMessage(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)    }

    override fun errorMessage(msg: String) {
        TODO("Not yet implemented")
    }

}