package com.example.timetrekerforandroid.fragment.navigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.navigation.PakingAdapter
import com.example.timetrekerforandroid.databinding.TasksFragmentBinding
import com.example.timetrekerforandroid.fragment.AddInformationFragment
import com.example.timetrekerforandroid.fragment.InformationFragment
import com.example.timetrekerforandroid.fragment.wps.Wps1Fragment
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.presenter.navigation.PackingPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.navigation.PackingView
import java.util.Locale

class PakingFragment: Fragment(), ScannerController.ScannerCallback, PackingView, PakingAdapter.OnClickItem {
    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: PackingPresenter
    private var adapter: PakingAdapter? = null
    private lateinit var mWaitDialog: WaitDialog

    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }
    private var originalData: List<ArticlesResponse.Articuls>? = null

    companion object {
        fun newInstance(): PakingFragment {
            return PakingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.name.text = SPHelper.getNameTask()
        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                val text = editable.toString()
                Log.d("PakingFragment", "Text entered: $text")
                filter(text)
            }
        })
    }


    private fun filter(text: String) {
        val filteredName: ArrayList<ArticlesResponse.Articuls?> = ArrayList()
        originalData?.let {
            val lowerCaseText = text.lowercase(Locale.getDefault())

            for (data in it) {
                val articulContains = data.artikul?.toString()?.contains(lowerCaseText) == true
                val articulSyryaContains = data.artikulSyrya?.toString()?.contains(lowerCaseText) == true
                val nameContains = data.nazvanieTovara?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true

                if (articulContains || nameContains || articulSyryaContains) {
                    filteredName.add(data)
                }
            }

            adapter?.setFilterData(filteredName)
        }
    }



    private fun filterOnSHK(text: String) {
        val filteredName: ArrayList<ArticlesResponse.Articuls?> = ArrayList()
        val lowerCaseText = text.lowercase(Locale.getDefault()).trim() // Convert text to lowercase and trim whitespace

        originalData?.let {
            for (data in it) {
                // Log the data being compared
                Log.d("FILTER_SHK", "Comparing with: ${data.shk}, ${data.shkSpo1}, ${data.artikul}, ${data.artikulSyrya}")

                val artikulContains = data.artikul?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
                val shkContains = data.shk?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
                val shkSpo1Contains = data.shkSpo1?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
                val shkArticulContains = data.artikulSyrya?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true

                // Log the results of the filter conditions
                Log.d("FILTER_SHK", "artikulContains: $artikulContains, shkContains: $shkContains, shkSpo1Contains: $shkSpo1Contains, shkArticulContains: $shkArticulContains")

                // Add data to the filtered list if any of the conditions match
                if (shkContains || shkSpo1Contains || artikulContains || shkArticulContains) {
                    filteredName.add(data)
                }
            }

            // Log the number of filtered results
            Log.d("FILTER", "Filtered data size: ${filteredName.size}")
            adapter?.setFilterData(filteredName)
        }
    }






    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onResume() {
        super.onResume()
        scannerController.resumeScanner()
        presenter = PackingPresenter(this)
        showDialog()
        presenter.getPackingData()
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

    override fun onDataReceived(barcodeData: String) {
        requireActivity().runOnUiThread {
            filterOnSHK(barcodeData)
            Log.d("BARCODE", barcodeData)
        }
    }

    override fun onScanFailed(errorMessage: String) {
        requireActivity().runOnUiThread {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun success(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun error(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun getData(data: List<ArticlesResponse.Articuls>) {
        mWaitDialog.dismiss()
        // Сохраняем оригинальные данные для последующей фильтрации
        originalData = ArrayList(data)

        // Создаем адаптер с полученными данными
        adapter = context?.let { PakingAdapter(it, data, this) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
    }

    override fun getLdu(lduList: List<Value>) {
        (activity as StartActivity).replaceFragment(InformationFragment.newInstance(lduList.first()), false)
    }


    override fun onClick(item: ArticlesResponse.Articuls) {
        scannerController.releaseScanner()
        SPHelper.setPrefics(item.pref)

        if (item.itogZakaz != null) {
            SPHelper.setItogZakaza(item.itogZakaz)
            SPHelper.setSizeTovara(item.itogZakaz)
        }
        SPHelper.setNameTask(item.nazvanieZadaniya)
        SPHelper.setArticuleWork(item.artikul.toString())
        SPHelper.setNameToavara(item.nazvanieTovara)
        SPHelper.setSyryo(item.artikulSyrya != null)
        if (item.artikulSyrya != null) {
            SPHelper.setArticulsSyryo(item.artikulSyrya)
            SPHelper.setSizeSyryo(item.kolVoSyrya)
        }
        SPHelper.setSrokGodnosti(item.srokGodnosti != null)
        Log.d("SROK_DODOSTI", SPHelper.getSrokGodnosti().toString())

        SPHelper.setNameStuffWork(item.nazvanieTovara)
        SPHelper.setShkWork(item.shk)

        presenter.getPackingDataLDU()


    }
}
