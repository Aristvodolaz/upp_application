package com.example.timetrekerforandroid.fragment.start

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
import com.example.timetrekerforandroid.databinding.InfoSyryoFragmentBinding
import com.example.timetrekerforandroid.dialog.ApproveShkDialog
import com.example.timetrekerforandroid.dialog.ApproveSrokGodnostiDialog
import com.example.timetrekerforandroid.dialog.CancelReasonDialog
import com.example.timetrekerforandroid.fragment.navigation.TasksFragment
import com.example.timetrekerforandroid.network.response.ArticlesResponse
import com.example.timetrekerforandroid.presenter.InfoArticlePresenter
import com.example.timetrekerforandroid.util.SPHelper
import com.example.timetrekerforandroid.util.WaitDialog
import com.example.timetrekerforandroid.view.InfoArticleView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class InfoSyryoFragment (private var name: String, private var article: String,
                         private var fullSize: String, private var articul_syrya: String,
                         private var kolvo_syrya: String): Fragment(), InfoArticleView, ApproveShkDialog.OnSendNewShk,
    ApproveSrokGodnostiDialog.OnSendApproveOnSrokGodnosti, CancelReasonDialog.OnCancelReasonSelected {

    private var _binding: InfoSyryoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: InfoArticlePresenter
    private lateinit var mWaitDialog: WaitDialog
    private var persent: String = ""
    private var date: String = ""

    companion object {
        fun newInstance(name: String, article: String, fullSize: String,  articul_syrya: String, kolvo_syrya: String): InfoSyryoFragment {
            return InfoSyryoFragment(name, article, fullSize, articul_syrya, kolvo_syrya)
        }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance()
        mWaitDialog.isCancelable = false
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = InfoSyryoFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            if((SPHelper.getSrokGodnosti() && SPHelper.getPrefics() == "WB") || SPHelper.getPrefics() == "OZON" || SPHelper.getPrefics() == "ЯМ")
                binding.btnCheck.visibility = View.VISIBLE
            else binding.btnCheck.visibility = View.GONE        }

        binding.btnClanced.setOnClickListener{
            showDialog()
            presenter.sendEndStatus()
        }

        if ((SPHelper.getSrokGodnosti() && SPHelper.getPrefics() == "WB") || SPHelper.getPrefics() == "OZON" || SPHelper.getPrefics() == "ЯМ") {
            binding.srok.visibility = View.VISIBLE
            binding.btnCheck.visibility = View.VISIBLE
        } else {
            binding.srok.visibility = View.GONE
            binding.btnCheck.visibility = View.GONE
            binding.btnNext.visibility = View.VISIBLE
        }

        binding.btnCheck.setOnClickListener {
            if ((SPHelper.getSrokGodnosti() && SPHelper.getPrefics() == "WB") || SPHelper.getPrefics() == "OZON" || SPHelper.getPrefics() == "ЯМ") {
                when {
                    binding.first.text.isNotEmpty() && binding.second.text.isNotEmpty() -> {
                        firstAndLastDate(binding.first.text.toString(), binding.second.text.toString())
                    }
                    binding.first.text.isNotEmpty() && binding.month.text.isNotEmpty() ->{
                        calculateExpirationDate(binding.first.text.toString(), binding.month.text.toString().toInt())
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



            binding.lineSyrya.visibility = View.VISIBLE
            binding.articulSyrya.text = "Артикул сырья: $articul_syrya"
            binding.kolvoSyrya.text = "Количество сырья: $kolvo_syrya"

        binding.btnNext.setOnClickListener{
            (activity as StartActivity).replaceFragment(ChooseOpFragment.newInstance(), false)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateExpirationDate(firstDateInput: String, months: Int) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val firstDate = dateFormat.parse(firstDateInput) ?: return

        val expirationDate = LocalDate.parse(firstDateInput, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .plusMonths(months.toLong())

        // Преобразуем в строку для отображения
        val secondDate = expirationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        visableDialogApprove(checkDate(firstDateInput, secondDate))
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
        (activity as StartActivity).replaceFragment(ChooseOpFragment.newInstance(), false)
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
        mWaitDialog.dismiss()
        binding.first.text.clear()
        Toast.makeText(context, "Введите последнюю дату", Toast.LENGTH_SHORT).show()
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Установите binding в null, чтобы избежать утечек памяти
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
        binding.btnNext.visibility = View.VISIBLE
    }

    override fun errorWriteSg() {
        mWaitDialog.dismiss()
        Toast.makeText(context, "Ошибка соединения, повторите попытку", Toast.LENGTH_SHORT).show()
    }

    override fun successEndStatus() {
        mWaitDialog.dismiss()
//        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }

    override fun sendNewShk(shk: String?) {
        if (shk != null) {
            presenter.updateShk(shk)
        }
    }



    override fun onReasonSelected(reason: String, comment: String) {
        presenter.cancelTask(reason, comment)
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }


}
