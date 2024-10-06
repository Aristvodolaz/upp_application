package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.navigation.ChooseOpAdapter
import com.example.timetrekerforandroid.databinding.InformationFragmentBinding
import com.example.timetrekerforandroid.factory.InformationModelFactory
import com.example.timetrekerforandroid.model.NetworkModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.util.InfoForCheckBox
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.viewModel.InformationViewModel
import com.example.timetrekerforandroid.fragment.wps.Wps1Fragment
import com.example.timetrekerforandroid.network.response.ChooseOpResponse

class InformationFragment(private val data: Value) : Fragment() {
    private var _binding: InformationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ChooseOpAdapter
    private lateinit var viewModel: InformationViewModel

    companion object {
        fun newInstance(data: Value): InformationFragment {
            return InformationFragment(data)
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
        // Initialize ViewModel
        viewModel = ViewModelProvider(
            this,
            InformationModelFactory(NetworkModel(ServiceViewModule.getService()))
        )[InformationViewModel::class.java]

        // Set layout manager

        // Initialize the adapter with an empty list and an empty map


        processResponse(data)
        // Load task dat

        // Additional UI updates
        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameToavara()
        if (SPHelper.getSyryo()) {
            binding.nameShk.text = "Сырьевые артикулы: ${SPHelper.getArticulsSyryo()}"
            binding.sizeSyrya.text = "Количество сырья:  ${SPHelper.getSizeSyryo()}"
        } else {
            binding.nameShk.text = "ШК товара: ${SPHelper.getShkWork()}"
            binding.sizeSyrya.visibility = View.GONE
        }

        // Set click listener for the button
        binding.btnSk.setOnClickListener {
            if (SPHelper.getPrefics() == "WB") {
                (activity as StartActivity).replaceFragment(Wps1Fragment.newInstance(), true)
            } else {
                (activity as StartActivity).replaceFragment(AddInformationFragment.newInstance(SPHelper.getSyryo()), true)
            }
        }


    }

    private fun processResponse(response: Value) {
        // Create a map from the response values
        val valuesMap = InfoForCheckBox.infoBoxToDB.associateWith { key ->
            getFieldValue(response, key) == "V"
        }

        // Log the map to ensure it is populated correctly
        Log.d("InformationFragment", "valuesMap: $valuesMap")

        // Filter the display values based on the values in infoBoxToDB (if they are true in valuesMap)
        val filteredDisplayValues = InfoForCheckBox.infoBoxToDB
            .mapIndexedNotNull { index, dbKey ->
                if (valuesMap[dbKey] == true) {
                    // Return the corresponding display value from infoBox
                    InfoForCheckBox.infoBox[index]
                } else {
                    null
                }
            }.toTypedArray()

        // Log the filtered values to ensure correct filtering
        Log.d("InformationFragment", "filteredDisplayValues: ${filteredDisplayValues.contentToString()}")

        adapter = ChooseOpAdapter(requireContext(), filteredDisplayValues, valuesMap)
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter  // Set the adapter before loading data
        // Update the adapter with new data
        adapter.setData(filteredDisplayValues, valuesMap)
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
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
