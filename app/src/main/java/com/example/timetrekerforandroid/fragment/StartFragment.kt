package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.NameFileAdapter
import com.example.timetrekerforandroid.databinding.StartsFragmentBinding
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.network.response.Data
import com.example.timetrekerforandroid.presenter.StartPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.StartView


class StartFragment: Fragment(), StartView, NameFileAdapter.OnClickItem {
    var _binding: StartsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: StartPresenter
    private var adapter: NameFileAdapter? = null
    private var wait: Boolean = true;
    private var isWaiting: Boolean = true
    private lateinit var mWaitDialog: WaitDialog

    companion object{
        fun newInstance() : StartFragment {return StartFragment() }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = StartsFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        presenter = StartPresenter(this)
        showDialog()
        presenter.getDataInWork()

        binding.name.text = SPHelper.getSklad()

        binding.btn.setOnClickListener{
            (activity as StartActivity).replaceFragment(ChooseSkladFragment.newInstance(), false)
        }
    }


    override fun getDataInWork(data: List<Data>) {
        mWaitDialog.dismiss()
        handlerAdapter(data)
    }

    override fun msg(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun handlerAdapter(data: List<Data>){
        adapter = context?.let{ NameFileAdapter(it, data, this) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
    }

    override fun onClick(name: String) {
        SPHelper.setPrefics(name.split(" ").first())
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(name), true);
    }


}