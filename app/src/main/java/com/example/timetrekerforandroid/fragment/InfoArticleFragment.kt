package com.example.timetrekerforandroid.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.timetrekerforandroid.activity.StartActivity
import com.example.timetrekerforandroid.activity.TaskActivity
import com.example.timetrekerforandroid.databinding.InfoArticleFragmentBinding
import com.example.timetrekerforandroid.dialog.ApproveShkDialog
import com.example.timetrekerforandroid.dialog.ApproveSrokGodnostiDialog
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.presenter.InfoArticlePresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.InfoArticleView
import com.symbol.emdk.EMDKManager
import com.symbol.emdk.EMDKManager.EMDKListener
import com.symbol.emdk.EMDKResults
import com.symbol.emdk.barcode.BarcodeManager
import com.symbol.emdk.barcode.ScanDataCollection
import com.symbol.emdk.barcode.Scanner
import com.symbol.emdk.barcode.ScannerException
import com.symbol.emdk.barcode.ScannerResults
import com.symbol.emdk.barcode.StatusData
import com.symbol.emdk.barcode.StatusData.ScannerStates
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class InfoArticleFragment(private var name: String, private var article: String,
                          private var fullSize: String, private var syryo: Boolean, private var articul_syrya: String,
                          private var kolvo_syrya: String): Fragment(), InfoArticleView, ApproveShkDialog.OnSendNewShk,
    EMDKListener, Scanner.DataListener, Scanner.StatusListener, ApproveSrokGodnostiDialog.OnSendApproveOnSrokGodnosti {

    private var _binding: InfoArticleFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: InfoArticlePresenter
    private var emdkManager: EMDKManager? = null
    private var barcodeManager: BarcodeManager? = null
    private var scanner: Scanner? = null
    private lateinit var mWaitDialog: WaitDialog
    private var persent: String = ""
    private var date: String = ""

    companion object {
        fun newInstance(name: String, article: String, fullSize: String, syryo: Boolean, articul_syrya: String, kolvo_syrya: String): InfoArticleFragment {
            return InfoArticleFragment(name, article, fullSize, syryo, articul_syrya, kolvo_syrya)
        }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val emdkResults = EMDKManager.getEMDKManager(requireActivity().applicationContext, this)
        if (emdkResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            Toast.makeText(context, "Error initializing EMDK", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = InfoArticleFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        binding.nameArticle.text = "Артикул товара: $article"
        SPHelper.setArticuleWork(article)
        binding.nameStuff.text = name
        binding.itog.text = "Итог заказа: $fullSize"

        presenter = InfoArticlePresenter(this)

        binding.btnWork.setOnClickListener {
            showDialog()
            presenter.changeStatusTask(article, 1)
            binding.btnWork.visibility = View.GONE
            binding.btnClanced.visibility = View.VISIBLE
            binding.btnCheck.visibility = View.VISIBLE
        }

        binding.btnClanced.setOnClickListener{
            showDialog()
            presenter.sendEndStatus()
        }

        if ((SPHelper.getSrokGodnosti() && SPHelper.getPrefics() == "WB") || SPHelper.getPrefics() == "OZON") {
            binding.srok.visibility = View.VISIBLE
        } else {
            binding.srok.visibility = View.GONE
        }

        binding.btnCheck.setOnClickListener {
            if ((SPHelper.getSrokGodnosti() && SPHelper.getPrefics() == "WB") || SPHelper.getPrefics() == "OZON") {
                when {
                    binding.first.text.isNotEmpty() && binding.second.text.isNotEmpty() -> {
                        firstAndLastDate(binding.first.text.toString(), binding.second.text.toString())
                    }
                    binding.first.text.isNotEmpty() -> {
                        showDialog()
                        presenter.searchArticleInDbForSG(article)
                    }
                    binding.second.text.isNotEmpty() -> {
                        lastDate()
                    }
                    else -> {
                        Toast.makeText(context, "Введите дату срока годности", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Срок годности не требуется для этого товара", Toast.LENGTH_SHORT).show()
            }
        }


        if(syryo){
            binding.lineSyrya.visibility = View.VISIBLE
            binding.articulSyrya.text = "Артикул сырья: $articul_syrya"
            binding.kolvoSyrya.text = "Количество сырья: $kolvo_syrya"
        } else binding.lineSyrya.visibility = View.GONE
    }

    private fun lastDate() {
        date = binding.second.text.toString()
        persent = "0"
        visableDialogApprove(date)
    }

    private fun firstAndLastDate(first: String, second: String) {
        visableDialogApprove(checkDate(first, second))
    }

    private fun checkDate(first: String, second: String): String {

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val firstDate: Date? = dateFormat.parse(first)
        val secondDate: Date? = dateFormat.parse(second)
        date = second
        val currentDate = Date()


        val diffInMillis = secondDate!!.time - firstDate!!.time
        val diffDays = diffInMillis / (1000 * 60 * 60 * 24)
        val pastInMillis = currentDate.time - firstDate.time
        val pastInDays = pastInMillis / (1000 * 60 * 60 * 24)


        val itog = pastInDays.toDouble() / diffDays * 100
        return String.format("%.2f%%", itog)
    }

    override fun getDataInfo(data: ArticlesResponse.Articuls) {
        // TODO: Implement this method
    }

    override fun errorMessage(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun successFindShk(msg: String) {
        mWaitDialog.dismiss()
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun success() {
        (activity as TaskActivity).replaceFragment(ChooseOpFragment.newInstance(), false)
    }

    override fun createNewShk(shk: String) {
        Log.d(" NENNENENENE", "DJKDJKFJKL")
        val dialog = ApproveShkDialog.newInstance(shk, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, "lol") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun checkLastPeriodDate(days: Int) {
        mWaitDialog.dismiss()
        visableDialogApprove(getPersent(days))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPersent(days: Int): String{
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val startDate = LocalDate.parse(binding.first.text.toString(), formatter)
        val currentDate = LocalDate.now()
        date = startDate.plusDays(days.toLong()).toString()

        val elapsedDays = ChronoUnit.DAYS.between(startDate, currentDate)

        val percentage = (elapsedDays.toDouble() / days.toDouble()) * 100

        return String.format("%.2f%%", percentage)
    }

    fun visableDialogApprove(text: String){
        persent = text
        val dialog = ApproveSrokGodnostiDialog.newInstance(text, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, ApproveSrokGodnostiDialog.TAG) }
    }

    override fun writeLastDate() {
        binding.first.text.clear()
        Toast.makeText(context, "Введите последнюю дату", Toast.LENGTH_SHORT).show()
    }


    override fun onOpened(manager: EMDKManager?) {
        emdkManager = manager
        barcodeManager = emdkManager?.getInstance(EMDKManager.FEATURE_TYPE.BARCODE) as BarcodeManager
        initializeScanner()
    }

    private fun initializeScanner() {
        if (scanner == null) {
            scanner = barcodeManager?.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT)
            scanner?.addDataListener(this)
            scanner?.addStatusListener(this)
            scanner?.triggerType = Scanner.TriggerType.HARD
            try {
                scanner?.enable()
            } catch (e: ScannerException) {
                Toast.makeText(context, "Сканер недоступен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(scanner!=null){
            try{
                scanner!!.cancelRead()
            }catch (e: ScannerException){
                Log.d("EEEE", e.localizedMessage)
            }
        }
    }

    override fun onClosed() {
        releaseScanner()
        emdkManager?.release()
        emdkManager = null
    }

    private fun releaseScanner() {
        try {
            scanner?.disable()
        } catch (e: ScannerException) {
            Toast.makeText(context, "Сканер недоступен", Toast.LENGTH_SHORT).show()
        }
        scanner?.removeDataListener(this)
        scanner?.removeStatusListener(this)
    }

    override fun onData(scanDataCollection: ScanDataCollection?) {
        if (scanDataCollection != null && scanDataCollection.result == ScannerResults.SUCCESS) {
            val scanDataList = scanDataCollection.scanData
            for (data in scanDataList) {
                val barcodeData = data.data
                requireActivity().runOnUiThread {
                    SPHelper.setShkWork(barcodeData)
                    presenter.findInExcel(barcodeData, SPHelper.getNameTask())
                    Log.d("BARCODE", barcodeData)
                }
            }
        }
    }


    override fun onStatus(statusData: StatusData?) {
        if (statusData != null) {
            when (statusData.getState()) {
                ScannerStates.IDLE -> try {
                    scanner!!.read()
                } catch (e: ScannerException) {
                    Toast.makeText(context, "Сканер недоступен", Toast.LENGTH_SHORT).show()
                }

                ScannerStates.WAITING -> {}
                ScannerStates.SCANNING -> {}
                ScannerStates.DISABLED -> {}
                ScannerStates.ERROR -> Toast.makeText(
                    context,
                    "Scanner error occurred",
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onResume() {
        super.onResume()
        if(emdkManager!=null && scanner != null){
            try {
                scanner!!.read()
            }catch (e: ScannerException){
                Log.d("EEEE", e.localizedMessage)
            }
        } else initializeScanner()
    }
    override fun sendApprove(approve: Boolean) {
        if(approve){
            Log.d("APPROVE", "TRUE")
            presenter.sendSrokGodnosti(date, persent)
        }  else {
            Log.d("APPROVE", "FALSE")
            presenter.sendEndStatus()
        }
    }

    override fun successWriteSG() {
        mWaitDialog.dismiss()
        Toast.makeText(context, "Срок годности успешно записан", Toast.LENGTH_SHORT).show()
        binding.btnCheck.visibility = View.GONE
        binding.srok.visibility = View.GONE
    }

    override fun errorWriteSg() {
        mWaitDialog.dismiss()
        Toast.makeText(context, "Ошибка соединения, повторите попытку", Toast.LENGTH_SHORT).show()
    }

    override fun successEndStatus() {
        mWaitDialog.dismiss()
        activity?.finish()
    }

    override fun sendNewShk(shk: String?) {
        if (shk != null) {
            presenter.updateShk(shk)
        }
    }


}
