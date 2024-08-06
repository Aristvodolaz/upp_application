package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.activity.TaskActivity
import com.example.timetrekerforandroid.adapter.ChooseOpAdapter
import com.example.timetrekerforandroid.databinding.ChooseOpFragmentBinding
import com.example.timetrekerforandroid.presenter.ChooseOpPresenter
import com.example.timetrekerforandroid.util.InfoForCheckBox.infoBox
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.ChooseOpView

class ChooseOpFragment : Fragment(), ChooseOpView {
    private var _binding: ChooseOpFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ChooseOpPresenter
    private var adapter: ChooseOpAdapter? = null
    private lateinit var mWaitDialog: WaitDialog

    companion object {
        fun newInstance(): ChooseOpFragment {
            return ChooseOpFragment()
        }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ChooseOpFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        presenter = ChooseOpPresenter(this)

        adapter = context?.let { ChooseOpAdapter(it, infoBox) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter

        binding.btnSk.setOnClickListener {
            val selectedItems = adapter?.getData()
            if (!selectedItems.isNullOrEmpty()) {
                showDialog()
                presenter.sendChooseOpInDB(selectedItems)
            } else {
                msgError("Список пуст, выберите данные для упаковки и попробуйте еще раз")
            }
        }
    }

    override fun msgError(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun msgSuccess(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        (activity as? TaskActivity)?.replaceFragment(AddInformationFragment.newInstance(), false)
    }
}