package com.example.timetrekerforandroid.fragment.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.fadeIn
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.ChooseOpAdapter
import com.example.timetrekerforandroid.databinding.ChooseOpFragmentBinding
import com.example.timetrekerforandroid.fragment.ScanSHKBoxFragment
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.network.response.ChooseOpResponse
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.presenter.ChooseOpPresenter
import com.example.timetrekerforandroid.util.InfoForCheckBox
import com.example.timetrekerforandroid.util.SPHelper
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

    override fun onResume() {
        super.onResume()
        presenter = ChooseOpPresenter(this)
        presenter.getData()

    }

    private fun initViews() {
        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameToavara()
        binding.size.text = "Количество товара: ${SPHelper.getSizeTovara()}"
        if(SPHelper.getSyryo()) {
            binding.nameShk.text = "Сырьевые артикулы: ${SPHelper.getArticulsSyryo()}"
            binding.sizeSyrya.text = "Количество сырья:  ${SPHelper.getSizeSyryo()}"
        } else{
            binding.nameShk.text = "ШК товара: ${SPHelper.getShkWork()}"
            binding.sizeSyrya.visibility = View.GONE
        }

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
        if(SPHelper.getPrefics().equals("WB")){
            (activity as StartActivity).replaceFragment(ScanSHKBoxFragment.newInstance(), true)
        } else (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)

    }

    override fun getData(data: ChooseOpResponse) {
        val firstValue = data.value[0]
        // Значения для галочек, которые нужно отобразить (заполняем через valuesMap)
        val displayValues = InfoForCheckBox.infoBox
        val valuesMap = InfoForCheckBox.infoBoxToDB.associateWith { key ->
            getFieldValue(firstValue, key) == "V"
        }

        adapter = context?.let { ChooseOpAdapter(it, displayValues, valuesMap) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
    }

    private fun getFieldValue(value: Value, fieldName: String): String? {
        return when (fieldName) {
            "Op_1_Bl_1_Sht" -> value.getOp1Bl1Sht()
            "Op_2_Bl_2_Sht" -> value.getOp2Bl2Sht()
            "Op_3_Bl_3_Sht" -> value.getOp3Bl3Sht()
            "Op_4_Bl_4_Sht" -> value.getOp4Bl4Sht()
            "Op_5_Bl_5_Sht" -> value.getOp5Bl5Sht()
            "Op_6_Blis_6_10_Sht" -> value.getOp6Blis610Sht()
            "Op_7_Pereschyot" -> value.getOp7Pereschyot()
            "Op_9_Fasovka_Sborka" -> value.getOp9FasovkaSborka()
            "Op_10_Markirovka_SHT" -> value.getOp10MarkirovkaSHT()
            "Op_11_Markirovka_Prom" -> value.getOp11MarkirovkaProm()
            "Op_12_Markirovka_Prom" -> value.getOp12MarkirovkaProm()
            "Op_13_Markirovka_Fabr" -> value.getOp13MarkirovkaFabr()
            "Op_14_TU_1_Sht" -> value.getOp14TU1Sht()
            "Op_15_TU_2_Sht" -> value.getOp15TU2Sht()
            "Op_16_TU_3_5" -> value.getOp16TU35()
            "Op_17_TU_6_8" -> value.getOp17TU68()
            "Op_468_Proverka_SHK" -> value.getOp468ProverkaSHK()
            "Op_469_Spetsifikatsiya_TM" -> value.getOp469SpetsifikatsiyaTM()
            "Op_470_Dop_Upakovka" -> value.getOp470DopUpakovka()
            else -> null
        }
    }
}
