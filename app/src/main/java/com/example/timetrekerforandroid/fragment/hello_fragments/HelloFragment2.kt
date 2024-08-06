package com.komus_lc.upp.fragment.hello_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.databinding.HelloFragmentTwoBinding

class HelloFragment2: Fragment() {
    var _binding: HelloFragmentTwoBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): HelloFragment2 {
            return HelloFragment2()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = HelloFragmentTwoBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {

    }
}