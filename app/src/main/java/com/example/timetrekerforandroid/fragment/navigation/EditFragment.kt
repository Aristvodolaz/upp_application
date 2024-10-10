package com.example.timetrekerforandroid.fragment.navigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.adapter.navigation.EditAdapter
import com.example.timetrekerforandroid.adapter.navigation.EditAdapterForWb
import com.example.timetrekerforandroid.databinding.EditFragmentBinding
import com.example.timetrekerforandroid.fragment.ChangeFragment
import com.example.timetrekerforandroid.fragment.edit.EditForWbFragment
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.network.response.DataWBResponse
import com.example.timetrekerforandroid.network.response.Value
import com.example.timetrekerforandroid.presenter.navigation.EditPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.navigation.EditView
import java.util.Locale

class EditFragment : Fragment(), ScannerController.ScannerCallback, EditView, EditAdapter.OnClickItem, EditAdapterForWb.OnClickItem {

    private var _binding: EditFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: EditPresenter
    private var adapter: EditAdapter? = null
    private var adapterWb: EditAdapterForWb? = null
    private lateinit var waitDialog: WaitDialog

    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }

    private var originalData: List<ArticlesResponse.Articuls>? = null
    private var originalDataWB: List<DataWBResponse>? = null

    companion object {
        fun newInstance(): EditFragment {
            return EditFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = EditFragmentBinding.inflate(inflater, container, false)
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
        presenter = EditPresenter(this)
    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(context)
    }

    private fun filter(text: String) {
        val lowerCaseText = text.lowercase(Locale.getDefault())
        if (SPHelper.getPrefics() == "WB") {
            adapterWb?.setFilterData(originalDataWB?.filter {
                it.artikul.toString().contains(lowerCaseText) || it.shk.contains(lowerCaseText)
            } ?: emptyList())
        } else {
            adapter?.setFilterData(originalData?.filter {
                it.artikul.toString().contains(lowerCaseText) ||
                        it.artikulSyrya?.toString()?.contains(lowerCaseText) == true ||
                        it.nazvanieTovara?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
            } ?: emptyList())
        }
    }

    override fun onResume() {
        super.onResume()
        scannerController.resumeScanner()
        presenter = EditPresenter(this)

        showDialog()
        if (SPHelper.getPrefics() == "WB") {
            presenter.getDataForWB()
        } else {
            presenter.getPackingData()
        }
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
        }
    }

    private fun filterOnSHK(text: String) {
        val lowerCaseText = text.lowercase(Locale.getDefault())
        adapter?.setFilterData(originalData?.filter {
            it.shk?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.shkSpo1?.toString()?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true ||
                    it.shkWps.toString().lowercase(Locale.getDefault()).contains(lowerCaseText)
        } ?: emptyList())
    }

    private fun showDialog() {
        waitDialog = WaitDialog.newInstance()
        waitDialog.isCancelable = false
        fragmentManager?.let { waitDialog.show(it, WaitDialog.TAG) }
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
        adapter = context?.let { EditAdapter(it, data, this) }
        binding.rv.adapter = adapter
    }

    override fun getDataWB(data: List<DataWBResponse>) {
        waitDialog.dismiss()
        originalDataWB = data
        adapterWb = context?.let { EditAdapterForWb(it, data, this) }
        binding.rv.adapter = adapterWb
    }

    override fun getLdu(lduList: List<Value>) {
        // Not yet implemented
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
        SPHelper.setNameStuffWork(item.nazvanieTovara)
        SPHelper.setShkWork(item.shk)

        presenter.getPackingDataLDU()

        (activity as StartActivity).replaceFragment(
            ChangeFragment.newInstance(
                item.nazvanieTovara.toString(),
                SPHelper.getArticuleWork(),
                item.mesto.toString(),
                SPHelper.getShkWork(),
                item.vlozhennost.toString(),
                item.palletNo.toString(),
                item.itogZakaz.toString()
            ),
            true
        )
    }

    override fun onClick(item: DataWBResponse) {
        (activity as StartActivity).replaceFragment(EditForWbFragment.newInstance(item), true)
    }
}