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
import com.example.timetrekerforandroid.util.ScannerManager
import com.example.timetrekerforandroid.util.ScannerManager.ScannerDataListener
import com.example.timetrekerforandroid.util.ScannerManager.ScannerStatusListener
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.TasksView
import java.util.Locale

class TasksFragment(private val name: String) : Fragment(), TasksView, ArtikulTasksAdapter.OnClickItem,
    ScannerDataListener, ScannerStatusListener {

    var _binding: TasksFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: TasksPresenter
    private var adapter: ArtikulTasksAdapter? = null
    private var scannerManager: ScannerManager? = null
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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerManager = ScannerManager.getInstance(requireActivity().applicationContext)
        scannerManager.setScannerDataListener(this)
        scannerManager.setScannerStatusListener(this)
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

    override fun onDataReceived(barcodeData: String?, barcodeType: String?) {
        requireActivity().runOnUiThread {
            showDialog()
            if (barcodeData != null) {
                filterOnSHK(barcodeData)
            }
        }
    }

    override fun onScanFailed(errorMessage: String?) {
        requireActivity().runOnUiThread {
            Toast.makeText(
                context,
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onStatusChanged(statusMessage: String?) {
        requireActivity().runOnUiThread {
            Toast.makeText(
                context,
                statusMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
