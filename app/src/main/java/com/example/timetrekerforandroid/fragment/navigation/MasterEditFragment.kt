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
import com.example.timetrekerforandroid.fragment.edit.MasterFragment
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.presenter.navigation.PackingPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.navigation.PackingView
import java.util.Locale

class MasterEditFragment : Fragment(), ScannerController.ScannerCallback, PackingView, PakingAdapter.OnClickItem {

    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: PackingPresenter
    private var adapter: PakingAdapter? = null
    private lateinit var waitDialog: WaitDialog

    private val scannerController by lazy { (requireActivity() as StartActivity).scannerController }
    private var originalData: List<ArticlesResponse.Articuls>? = null

    companion object {
        fun newInstance(): MasterEditFragment = MasterEditFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        setupPresenter()
        setupRecyclerView()
    }

    private fun initViews() {
        binding.name.text = SPHelper.getNameTask()
        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun setupPresenter() {
        presenter = PackingPresenter(this)
    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(context)
    }

    private fun filter(text: String) {
        val lowerCaseText = text.lowercase(Locale.getDefault())
        val filteredList = originalData?.filter {
            it.artikul?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.artikulSyrya?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.nazvanieTovara?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.shkSyrya?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.shk?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
        } ?: emptyList()
        adapter?.setFilterData(filteredList)
    }

    private fun filterOnSHK(text: String) {
        val lowerCaseText = text.lowercase(Locale.getDefault()).trim()
        val filteredList = originalData?.filter {
                    it.shk?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.shkSyrya?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
        } ?: emptyList()
        Log.d("FILTER", "Filtered data size: ${filteredList.size}")
        adapter?.setFilterData(filteredList)
    }

    private fun showDialog() {
        waitDialog = WaitDialog.newInstance()
        waitDialog.isCancelable = false
        fragmentManager?.let { waitDialog.show(it, WaitDialog.TAG) }
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
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun error(msg: String) {
        waitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun getData(data: List<ArticlesResponse.Articuls>) {
        waitDialog.dismiss()
        originalData = data
        adapter = context?.let { PakingAdapter(it, data, this) }
        binding.rv.adapter = adapter
    }

    override fun getLdu(data: List<Value>) {
        (activity as StartActivity).replaceFragment(MasterFragment.newInstance(data.first()), false)
    }

    override fun onClick(item: ArticlesResponse.Articuls) {
        scannerController.releaseScanner()
        SPHelper.setPrefics(item.pref)

        item.itogZakaz?.let {
            SPHelper.setItogZakaza(it)
            SPHelper.setSizeTovara(it)
        }
        SPHelper.setNameTask(item.nazvanieZadaniya)
        SPHelper.setArticuleWork(item.artikul?.toString() ?: "")
        SPHelper.setNameToavara(item.nazvanieTovara)
        SPHelper.setSyryo(item.artikulSyrya != null)

        item.artikulSyrya?.let {
            SPHelper.setArticulsSyryo(it)
            SPHelper.setSizeSyryo(item.kolVoSyrya)
        }
        SPHelper.setSrokGodnosti(item.srokGodnosti != null)
        SPHelper.setNameStuffWork(item.nazvanieTovara)
        SPHelper.setShkWork(item.shk)

        presenter.getPackingDataLDU()
    }
}