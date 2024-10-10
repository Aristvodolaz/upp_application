package com.example.timetrekerforandroid.fragment.wps

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.databinding.WpsTwoFragmentBinding
import com.example.timetrekerforandroid.factory.WpsModelFactory
import com.example.timetrekerforandroid.model.WpsModel
import com.example.timetrekerforandroid.network.ServiceViewModule
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.viewModel.WpsViewModel

class Wps2Fragment : Fragment(), ScannerController.ScannerCallback {

    private var _binding: WpsTwoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var wpsModel: WpsViewModel
    private val scannerController by lazy { (requireActivity() as StartActivity).getScannerController() }

    private var vlozhennost: Int = 0 // Переменная для хранения вложенности

    companion object {
        fun newInstance(): Wps2Fragment {
            return Wps2Fragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WpsTwoFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        // Инициализация ViewModel с фабрикой
        wpsModel = ViewModelProvider(
            this,
            WpsModelFactory(WpsModel(ServiceViewModule.getService()))
        )[WpsViewModel::class.java]

        // Отображение информации о товаре
        setupProductInfo()

        // Скрыть элементы для сканирования до ввода вложенности
        setInitialVisibility()

        // Установка обработчика для кнопки "Далее" (вместо "Записать")
        binding.btnDone.setText("Далее")
        binding.btnDone.setOnClickListener {
            handleNextButtonClick()
        }

        // Наблюдение за результатами добавления записи
        observeBoxData()
    }

    private fun setupProductInfo() {
        binding.nameArticle.text = "Артикул товара: ${SPHelper.getArticuleWork()}"
        binding.nameStuff.text = SPHelper.getNameStuffWork()
        binding.nameShk.text = "Итог заказа: ${SPHelper.getSizeTovara()}"
    }

    private fun setInitialVisibility() {
        binding.scanBox.visibility = View.GONE
        binding.line.visibility = View.VISIBLE // Показываем поле для ввода вложенности
        binding.btnDone.visibility = View.VISIBLE
    }

    private fun handleNextButtonClick() {
        // Извлечение и проверка вложенности
        vlozhennost = binding.vlozhennostEt.text.toString().toIntOrNull() ?: 0

        if (vlozhennost <= 0) {
            Toast.makeText(context, "Введите корректную вложенность", Toast.LENGTH_SHORT).show()
            return
        }

        // Если вложенность введена, переходим к сканированию паллета
        Log.d("Wps2Fragment", "Вложенность введена: $vlozhennost. Переход к сканированию паллета.")
        binding.line.visibility = View.GONE // Скрываем поле для ввода вложенности
        binding.scanBox.visibility = View.VISIBLE // Показываем блок для сканирования паллета
        scannerController.resumeScanner() // Запускаем сканер
    }

    // Колбэк при получении данных от сканера (штрих-кода паллета)
    override fun onDataReceived(barcodeData: String?) {
        if (barcodeData.isNullOrEmpty()) {
            Log.e("Wps2Fragment", "Пустой или некорректный штрих-код")
            Toast.makeText(context, "Некорректный штрих-код", Toast.LENGTH_SHORT).show()
            return
        }

        SPHelper.setShkPalleta(barcodeData)
        Log.d("Wps2Fragment", "Штрих-код паллета получен: $barcodeData")

        // После сканирования паллета сохраняем данные
        val shkWork = SPHelper.getShkWork().orEmpty()
        val shkPalleta = barcodeData

        Log.d("Wps2Fragment", "Передаем данные: shkWork = $shkWork, vlozhennost = $vlozhennost, shkPalleta = $shkPalleta")

        // Передача данных во ViewModel
        wpsModel.addZapisWithWps(shkWork, vlozhennost, shkPalleta.toString())

        wpsModel.errorMessage.observe(this, Observer { errorMsg ->
            errorMsg?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        wpsModel.boxData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { response ->
                // Успешная обработка данных
                (activity as StartActivity).replaceFragment(Wps1Fragment.newInstance(), false)
            }.onFailure { error ->
                // Отображаем более точное сообщение об ошибке
                Toast.makeText(context, error.message ?: "Неизвестная ошибка", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun observeBoxData() {
        wpsModel.boxData.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Log.d("Wps2Fragment", "Данные успешно отправлены")
                navigateToWps1Fragment()
            }.onFailure { error ->
                Log.e("Wps2Fragment", "Ошибка: ${error.message}")
                Toast.makeText(context, "Ошибка соединения: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToWps1Fragment() {
        // Переход на Wps1Fragment после успешной записи данных
        (activity as StartActivity).replaceFragment(Wps1Fragment.newInstance(), false)
    }

    override fun onScanFailed(errorMessage: String?) {
        Toast.makeText(context, errorMessage ?: "Ошибка сканирования", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        scannerController.resumeScanner()
    }

    override fun onPause() {
        super.onPause()
        scannerController.releaseScanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scannerController.releaseScanner()
        _binding = null
    }
}
