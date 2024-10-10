package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.databinding.ChangeFragmentBinding
import com.example.timetrekerforandroid.fragment.navigation.EditFragment
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.presenter.ChangePresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.view.ChangeView

class ChangeFragment(private var name: String, private  var article: String, private var mesto: String, private var shk: String,
    private var vlozhennost: String, private var pallet: String, private var size: String): Fragment(), ChangeView {
    private var _binding: ChangeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: ChangePresenter


    companion object {
        fun newInstance(name: String, article: String, mesto: String, shk: String, vlozhennost: String, pallet: String, size: String): ChangeFragment {
            return ChangeFragment(name, article,mesto,  shk, vlozhennost, pallet,size)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ChangeFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        presenter = ChangePresenter(this)

        binding.nameStuff.text = name
        binding.nameArticle.text = "Артикул товара: $article"
        binding.nameShk.text = "ШК товара: $shk"
        binding.size.text = "Итог заказа: $size"

//        binding.size.text = "Количество товара: $fullSize"
//        binding.mestoEt.setText(mest)
        binding.vlozhennostEt.setText(vlozhennost)
        binding.paletEt.setText(pallet)
        binding.mestoEt.setText(mesto)

        SPHelper.setShkWork(shk)

        binding.btnDone.setOnClickListener{
            if(binding.mestoEt.text.isNotEmpty() && binding.paletEt.text.isNotEmpty() && binding.vlozhennostEt.text.isNotEmpty()){
                presenter.sendFinishedInformation(
                    binding.mestoEt.text.toString(),
                    binding.vlozhennostEt.text.toString(),
                    binding.paletEt.text.toString()
                )
            }
        }
    }

    override fun success() {
        (activity as StartActivity).replaceFragment(EditFragment.newInstance(), false)
    }

    override fun error(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}