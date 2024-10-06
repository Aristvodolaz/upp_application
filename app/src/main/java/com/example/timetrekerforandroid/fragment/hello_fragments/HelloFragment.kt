package com.example.timetrekerforandroid.fragment.hello_fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.timetrekerforandroid.databinding.HelloFragmentBinding


class HelloFragment: Fragment() {
    var _binding: HelloFragmentBinding? = null
    private val binding get() = _binding!!

    companion object{
        fun newInstance() : HelloFragment {return HelloFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = HelloFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        //todo сюда добавим переключение на авторизации скан/ либо по логину паролю(надо обсудить)
//
//        binding.btnSk.setOnClickListener{
//            (activity as StartActivity).replaceFragment(HelloFragment2.Companion.newInstance(), true);
//        //            openForDevice(SPHelper.getTypeMobileDevice())
//        }
//
//        binding.btnAuth.setOnClickListener{
//            openForDevice(SPHelper.getTypeMobileDevice())
//        }
    }

    private fun openForDevice(mobile: Boolean){
//        if(mobile) startActivity(Intent(activity, ScanMobileActivity::class.java))
//        else startActivity(Intent(activity, ScanTsdActivity::class.java))
    }
}