package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.adapter.ChooseOpAdapter
import com.example.timetrekerforandroid.databinding.AddInformationFragmentBinding
import com.example.timetrekerforandroid.databinding.ChooseOpFragmentBinding
import com.example.timetrekerforandroid.presenter.AddInformationPresenter
import com.example.timetrekerforandroid.presenter.ChooseOpPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.AddInformationView
class AddInformationFragment : Fragment(), AddInformationView {

    private var _binding: AddInformationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: AddInformationPresenter
    private var isNonStandard: Boolean = false
    private lateinit var waitDialog: WaitDialog

    companion object {
        fun newInstance(): AddInformationFragment = AddInformationFragment()
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
        activity?.finish()
    }

    override fun msgSuccessDuplicate(msg: String) {
        waitDialog.dismiss()
        binding.nestandartVlozhLiner.visibility = View.GONE
    }

    override fun msgError(msg: String) {
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
