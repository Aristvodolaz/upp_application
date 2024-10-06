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
import com.example.timetrekerforandroid.adapter.navigation.ArtikulTasksAdapter
import com.example.timetrekerforandroid.databinding.TasksFragmentBinding
import com.example.timetrekerforandroid.fragment.AddInformationFragment
import com.example.timetrekerforandroid.fragment.ChangeFragment
import com.example.timetrekerforandroid.fragment.start.InfoArticleFragment
import com.example.timetrekerforandroid.fragment.start.InfoSyryoFragment
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.presenter.TasksPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.ScannerController
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.TasksView
import java.util.Locale
class TasksFragment(private val name: String) : Fragment(), TasksView, ArtikulTasksAdapter.OnClickItem,
    ScannerController.ScannerCallback {

    private var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: TasksPresenter
    private var adapter: ArtikulTasksAdapter? = null
    private lateinit var mWaitDialog: WaitDialog
    private val scannerController by lazy {
        (requireActivity() as StartActivity).scannerController
    }
    private var originalData: List<ArticlesResponse.Articuls>? = null

    companion object {
        fun newInstance(name: String): TasksFragment {
            return TasksFragment(name)
        }
    }

    private fun initViews() {
        binding.name.text = name

        binding.et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                filter(editable.toString())
            }
        })
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    private fun filter(text: String) {
        val filteredName: ArrayList<ArticlesResponse.Articuls?> = ArrayList()
        originalData?.let {
            val lowerCaseText = text.lowercase(Locale.getDefault())

            for (data in it) {
                val articulContains = data.artikul?.toString()?.contains(lowerCaseText) == true
                val articulSyryaContains = data.artikulSyrya?.toString()?.contains(lowerCaseText) == true
                val nameContains = data.nazvanieTovara?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true
                val nameSHK = data.shk?.lowercase(Locale.getDefault())?.contains(lowerCaseText) == true

                if (articulContains || nameContains || articulSyryaContains || nameSHK) {
                    filteredName.add(data)
                }
            }

            adapter?.setFilterData(filteredName)
        }
    }



    private fun filterOnSHK(text: String) {
        val filteredName: ArrayList<ArticlesResponse.Articuls?> = ArrayList()
        originalData?.let {
            for (data in it) {
                val articulContains = data.artikul?.toString()?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true
                val shkContains = data.shk?.toString()?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true
                val shkSpo1Contains = data.shkSpo1?.toString()?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true
                if (shkContains || shkSpo1Contains || articulContains) {
                    filteredName.add(data)
                }
            }
            Log.d("FILTER", "Filtered data size: ${filteredName.size}") // Debugging log
            adapter?.setFilterData(filteredName)
        }
    }

    override fun getData(tasks: List<ArticlesResponse.Articuls>) {
        mWaitDialog.dismiss()
        originalData = ArrayList(tasks)
        adapter = context?.let { ArtikulTasksAdapter(it, tasks, this) }
        binding.rv.layoutManager = LinearLayoutManager(context)
        binding.rv.adapter = adapter
    }

    override fun msg(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onClick(item: ArticlesResponse.Articuls) {
        scannerController.releaseScanner()
        SPHelper.setPrefics(item.pref)
        Log.d("SPSPSPSPSP", item.pref)
        if(item.itogZakaz !=null) {
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
        SPHelper.setShkWork(item.shk)
        Log.d("SROK_DODOSTI", SPHelper.getSrokGodnosti().toString())

        var syryo = false
        var articul_syrya = ""
        var articul_kolvo = ""

        if (item.artikulSyrya != null) {
            syryo = true
            articul_syrya =  item.artikulSyrya
        }

        if (item.kolVoSyrya != null) articul_kolvo =  item.kolVoSyrya


//        if(item.status == 3){
//                SPHelper.setNameStuffWork(item.nazvanieTovara)
//                SPHelper.setArticuleWork(item.articul.toString())
//            (activity as StartActivity).replaceFragment(
//                AddInformationFragment.newInstance(syryo), false
//            )
//        }else if(item.status == 2){
//            (activity as StartActivity).replaceFragment(
//                ChangeFragment.newInstance(
//                    item.nazvanieTovara, item.articul.toString(),
//                    item.shk,
//                    item.vlozhennost.toString(), item.pallet.toString()
//                ), false
//            )
//        } else if(item.status !=3 && item.status!=2) {
//            println(syryo)
                if (syryo)
                    (activity as StartActivity).replaceFragment(
                        InfoSyryoFragment.newInstance(
                            item.nazvanieTovara,
                            item.artikul.toString(),
                            item.itogZakaz.toString(),
                            articul_syrya,
                            articul_kolvo
                        ), false
                    )
                else
                    (activity as StartActivity).replaceFragment(
                        InfoArticleFragment.newInstance(
                            item.nazvanieTovara, item.artikul.toString(), item.itogZakaz.toString()
                        ), false
                    )
//            }


        Log.d("LLLLLLL", item.artikulSyrya + " " + item.kolVoSyrya)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? StartActivity)?.let {
            it.scannerController.resumeScanner()
            it.updateBottomNavigationVisibility()  // Обновление видимости навигации
        }
        presenter = TasksPresenter(this)
        showDialog()
        presenter.getArtikules(name)
    }


    override fun onPause() {
        super.onPause()
        scannerController.releaseScanner()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scannerController.releaseScanner()
        _binding = null // Установите binding в null, чтобы избежать утечек памяти
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
}
