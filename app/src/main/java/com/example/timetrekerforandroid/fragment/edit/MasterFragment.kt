package com.example.timetrekerforandroid.fragment.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.master.ChooseOpAdapter
import com.example.timetrekerforandroid.databinding.InformationFragmentBinding
import com.example.timetrekerforandroid.factory.InformationModelFactory
import com.example.timetrekerforandroid.fragment.navigation.MasterEditFragment
import com.example.timetrekerforandroid.model.NetworkModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.network.request.ChooseOpRequest
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.util.InfoForCheckBox
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.viewModel.InformationViewModel

class MasterFragment(private val data: Value) : Fragment() {
    private var _binding: InformationFragmentBinding? = null
    private val binding get() = _binding!!
    private var adapter: ChooseOpAdapter? = null
    private lateinit var viewModel: InformationViewModel

    companion object {
        fun newInstance(data: Value): MasterFragment {
            return MasterFragment(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = InformationFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        viewModel = ViewModelProvider(
            this,
            InformationModelFactory(NetworkModel(ServiceViewModule.getService()))
        )[InformationViewModel::class.java]

        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameToavara()
        if (SPHelper.getSyryo()) {
            binding.nameShk.text = "Сырьевые артикулы: ${SPHelper.getArticulsSyryo()}"
            binding.sizeSyrya.text = "Количество сырья: ${SPHelper.getSizeSyryo()}"
        } else {
            binding.nameShk.text = "ШК товара: ${SPHelper.getShkWork()}"
            binding.sizeSyrya.visibility = View.GONE
        }

        val displayValues = InfoForCheckBox.infoBox
        val valuesMap = InfoForCheckBox.infoBoxToDB.associateWith { key ->
            getFieldValue(data, key) == "V"
        }

        adapter = context?.let { ChooseOpAdapter(it, displayValues, valuesMap) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter

        binding.name.text = "Режим редактирования"
        setSwitchesEnabled(true)

        binding.btnSk.setOnClickListener {
            val selectedValues = adapter?.getValuesForServer() ?: emptyMap()
            val chooseOpRequest = ChooseOpRequest(
                selectedValues["Op_1_Bl_1_Sht"],
                selectedValues["Op_2_Bl_2_Sht"],
                selectedValues["Op_3_Bl_3_Sht"],
                selectedValues["Op_4_Bl_4_Sht"],
                selectedValues["Op_5_Bl_5_Sht"],
                selectedValues["Op_6_Blis_6_10_Sht"],
                selectedValues["Op_7_Pereschyot"],
                selectedValues["Op_9_Fasovka_Sborka"],
                selectedValues["Op_10_Markirovka_SHT"],
                selectedValues["Op_11_Markirovka_Prom"],
                selectedValues["Op_12_Markirovka_Prom"],
                selectedValues["Op_13_Markirovka_Fabr"],
                selectedValues["Op_14_TU_1_Sht"],
                selectedValues["Op_15_TU_2_Sht"],
                selectedValues["Op_16_TU_3_5"],
                selectedValues["Op_17_TU_6_8"],
                selectedValues["Op_468_Proverka_SHK"],
                selectedValues["Op_469_Spetsifikatsiya_TM"],
                selectedValues["Op_470_Dop_Upakovka"]
            )
            viewModel.setUpdateLDU(chooseOpRequest)
        }

        viewModel.finishData.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Log.d("MasterFragment", "Data sent successfully")
                Log.d("MasterFragment", "Selected values: ${adapter?.getValuesForServer()}")
                adapter?.setData(emptyList()) // Очистка
                (activity as StartActivity).replaceFragment(MasterEditFragment.newInstance(), false)
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setSwitchesEnabled(enabled: Boolean) {
        adapter?.setSwitchesEnabled(enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
