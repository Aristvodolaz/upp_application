package com.example.timetrekerforandroid.fragment

import android.content.Intent
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
import com.example.timetrekerforandroid.activity.TaskActivity
import com.example.timetrekerforandroid.adapter.ArtikulTasksAdapter
import com.example.timetrekerforandroid.databinding.TasksFragmentBinding
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.presenter.TasksPresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.TasksView
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.EMDKResults
import com.symbol.emdk.barcode.BarcodeManager
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.Scanner
import com.symbol.emdk.barcode.ScannerException
import com.symbol.emdk.barcode.ScannerResults
import com.symbol.emdk.barcode.StatusData
import java.util.Locale
class TasksFragment(private val name: String) : Fragment(), TasksView, ArtikulTasksAdapter.OnClickItem,
    EMDKManager.EMDKListener, Scanner.DataListener, Scanner.StatusListener {

    var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: TasksPresenter
    private var adapter: ArtikulTasksAdapter? = null
    private var emdkManager: EMDKManager? = null
    private var barcodeManager: BarcodeManager? = null
    private var scanner: Scanner? = null
    private var originalData: List<ArticlesResponse.Articuls>? = null
    private lateinit var mWaitDialog: WaitDialog

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    companion object {
        fun newInstance(name: String): TasksFragment {
            return TasksFragment(name)
        }
    }



    override fun onResume() {
        super.onResume()
            if(emdkManager!=null && scanner != null) {
                Log.d("TRUE", "KDKDKDKKDKD")
                try {
                    scanner!!.read()
                } catch (e: ScannerException) {
                    Log.d("EEEE", e.localizedMessage)
                }
            }
            presenter = TasksPresenter(this)
            showDialog()
            presenter.getArtikules(name)
    }
    override fun onOpened(manager: EMDKManager?) {
        emdkManager = manager
        barcodeManager = emdkManager?.getInstance(EMDKManager.FEATURE_TYPE.BARCODE) as BarcodeManager
        initializeScanner()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val emdkResults = EMDKManager.getEMDKManager(requireActivity().applicationContext, this)
        if (emdkResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Toast.makeText(context, "Error initializing EMDK", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = TasksFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
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

    private fun filter(text: String) {
        val filteredName: ArrayList<ArticlesResponse.Articuls?> = ArrayList()
        originalData?.let {
            for (data in it) {
                if (data.articul.toString().contains(text.lowercase(Locale.getDefault()))) {
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
                if (data.shk.toString().contains(text.lowercase(Locale.getDefault())) ||
                    data.shkSpo1.toString().contains(text.lowercase(Locale.getDefault()))) {
                    filteredName.add(data)
                }
            }
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
        SPHelper.setPrefics(item.pref)
        SPHelper.setItogZakaza(item.itogZakaz)
        SPHelper.setSrokGodnosti(item.srokGodnosti != null)

        val intent = Intent(activity, TaskActivity::class.java)
        intent.putExtra("article", item.articul.toString())
        intent.putExtra("name_stuff", item.nazvanieTovara)
        intent.putExtra("size", item.itogZakaz.toString())

        if(item.articulSyrya!=null) {
            intent.putExtra("syryo", true)
            intent.putExtra("articul_syrya", item.articulSyrya)
        } else {
            intent.putExtra("syryo", false)
            intent.putExtra("articul_syrya", "")
        }

        if(item.kolvoSyrya!=null) intent.putExtra("articul_kolvo", item.kolvoSyrya)
        else intent.putExtra("articul_kolvo", "")
        Log.d("LLLLLLL", item.articulSyrya + " " + item.kolvoSyrya)
        startActivity(intent)
    }



    private fun initializeScanner() {
        if (scanner == null) {
            scanner = barcodeManager?.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT)
            scanner?.apply {
                addDataListener(this@TasksFragment)
                addStatusListener(this@TasksFragment)
                triggerType = Scanner.TriggerType.HARD
                try {
                    enable()
                } catch (e: ScannerException) {
                    Toast.makeText(context, "Error enabling scanner", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        releaseScanner()
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

    override fun onStop() {
        super.onStop()
        releaseScanner()
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

    override fun onPause() {
        super.onPause()
//        if (scanner != null) {
//           try{
//               scanner!!.cancelRead()
//           } catch (e: ScannerException){
//               Log.e("EXEPTION", e.localizedMessage)
//           }
//        }
        releaseScanner()
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }
    private fun releaseScanner() {
        try {
            scanner?.apply {
                disable()
                removeDataListener(this@TasksFragment)
                removeStatusListener(this@TasksFragment)
            }
        } catch (e: ScannerException) {
            Toast.makeText(context, "Error disabling scanner", Toast.LENGTH_SHORT).show()
        }
        scanner = null
    }

    override fun onData(scanDataCollection: ScanDataCollection?) {
        scanDataCollection?.let {
            if (it.result == ScannerResults.SUCCESS) {
                for (data in it.scanData) {
                    val barcodeData = data.data
                    requireActivity().runOnUiThread {
                        filterOnSHK(barcodeData)
                        Log.d("BARCODE", barcodeData)
                    }
                }
            }
        }
    }

    override fun onStatus(statusData: StatusData?) {
        statusData?.let {
            when (it.state) {
                StatusData.ScannerStates.IDLE -> try {
                    scanner?.read()
                } catch (e: ScannerException) {
                    Toast.makeText(context, "Error starting scan", Toast.LENGTH_SHORT).show()
                }
                StatusData.ScannerStates.WAITING, StatusData.ScannerStates.SCANNING, StatusData.ScannerStates.DISABLED -> {}
                StatusData.ScannerStates.ERROR -> Toast.makeText(context, "Scanner error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClosed() {
        releaseScanner()
        if (emdkManager != null) {
            emdkManager!!.release()
            emdkManager = null
        }
    }

}
