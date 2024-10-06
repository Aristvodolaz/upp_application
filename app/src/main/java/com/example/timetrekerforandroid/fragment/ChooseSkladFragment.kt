package com.example.timetrekerforandroid.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.timetrekerforandroid.R
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.factory.WpsModelFactory
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.network.response.Sklads
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class ChooseSkladFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var wpsModel: WpsViewModel
    private lateinit var skladsList: List<Sklads>

    companion object {
        fun newInstance(): ChooseSkladFragment {
            return ChooseSkladFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.choose_sklad_fragment, container, false)

        spinner = view.findViewById(R.id.spinner)
        saveButton = view.findViewById(R.id.saveButton)

        // Инициализация ViewModel
        wpsModel = ViewModelProvider(
            this,
            WpsModelFactory(WpsModel(ServiceViewModule.getService()))
        )[WpsViewModel::class.java]

        // Запрос данных о складах
        wpsModel.getSklads()

        // Наблюдаем за результатом запроса данных о складах
        wpsModel.skladData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                skladsList = response // Сохраняем список складов
                setupSpinner(skladsList) // Настраиваем Spinner с данными
            }.onFailure { error ->
                Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show()
            }
        })

        saveButton.setOnClickListener {
            val selectedItem = spinner.selectedItem.toString()
            if (selectedItem == "Выберите вариант") {
                Toast.makeText(requireContext(), "Пожалуйста, выберите вариант", Toast.LENGTH_SHORT).show()
            } else {
                // Найдем выбранный склад по его отображаемому значению и сохраним его Pref
                val selectedSklad = skladsList[spinner.selectedItemPosition - 1] // -1 чтобы учесть элемент "Выберите вариант"
                SPHelper.setSklad(selectedSklad.pref)
                Toast.makeText(requireContext(), "Склад выбран: ${selectedSklad.pref}", Toast.LENGTH_SHORT).show()
                (activity as StartActivity).replaceFragment(StartFragment.newInstance(), false)

            }
        }

        return view
    }

    // Метод для настройки Spinner
    private fun setupSpinner(sklads: List<Sklads>) {
        // Создаем список с отображаемым текстом "Pref - City"
        val items = mutableListOf("Выберите вариант")
        items.addAll(sklads.map { "${it.pref} - ${it.city}" })

        // Настраиваем адаптер для Spinner
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
