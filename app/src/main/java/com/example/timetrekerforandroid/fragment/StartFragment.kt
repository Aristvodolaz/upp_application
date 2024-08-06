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
import com.example.timetrekerforandroid.databinding.StartFragmentBinding
import com.example.timetrekerforandroid.databinding.StartsFragmentBinding
import com.example.timetrekerforandroid.presenter.StartPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.StartView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener


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
        setupTabs()
        setupTabSelectionHandler()

        presenter = StartPresenter(this)
        showDialog()
        presenter.getDataInWait()
    }

    private fun setupTabs() {
        val tab1 = binding.tab.newTab().setText("В ожидании")
        val tab2 = binding.tab.newTab().setText("В работе")

        binding.tab.apply {
            addTab(tab1)
            addTab(tab2)
        }
    }

    private fun setupTabSelectionHandler() {
        binding.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                isWaiting = tab.position == 0
                showDialog()
                if (isWaiting) {
                    wait = true
                    presenter.getDataInWait()
                } else {
                    wait = false
                    presenter.getDataInWork()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }


    override fun getDataInWait(data: List<String>) {
        mWaitDialog.dismiss()
        handlerAdapter(data)
    }

    override fun getDataInWork(data: List<String>) {
        mWaitDialog.dismiss()
        handlerAdapter(data)
    }

    override fun msg(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    private fun handlerAdapter(data: List<String>){
        adapter = context?.let{ NameFileAdapter(it, data, this) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
    }

    override fun onClick(name: String) {
        if(wait){
            showDialog()
            presenter.downloadExcel(name)
        } else {
            (activity as StartActivity).replaceFragment(TasksFragment.newInstance(name), true);
        }
    }


}