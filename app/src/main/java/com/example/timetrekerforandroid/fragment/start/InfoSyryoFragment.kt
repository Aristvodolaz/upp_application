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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class InfoSyryoFragment(private var name: String, private var article: String,
                        private var fullSize: String, private var articulSyrya: String,
                        private var kolvoSyrya: String): Fragment(), InfoArticleView, ApproveShkDialog.OnSendNewShk,
    ApproveSrokGodnostiDialog.OnSendApproveOnSrokGodnosti, CancelReasonDialog.OnCancelReasonSelected {

    private var _binding: InfoSyryoFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter: InfoArticlePresenter
    private lateinit var mWaitDialog: WaitDialog
    private var persent: String = ""
    private var date: String = ""
    var buffer: Boolean = false
    var buffer_two: Boolean = false
    companion object {
        fun newInstance(name: String, article: String, fullSize: String, articulSyrya: String, kolvoSyrya: String): InfoSyryoFragment {
            return InfoSyryoFragment(name, article, fullSize, articulSyrya, kolvoSyrya)
        }
    }

    private fun showDialog() {
        mWaitDialog = WaitDialog.newInstance().apply { isCancelable = false }
        fragmentManager?.let { mWaitDialog.show(it, WaitDialog.TAG) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = InfoSyryoFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        with(binding) {
            nameArticle.text = "Артикул товара: $article"
            SPHelper.setArticuleWork(article)
            nameStuff.text = name
            itog.text = "Итог заказа: $fullSize"

            presenter = InfoArticlePresenter(this@InfoSyryoFragment)

            btnWork.setOnClickListener {
                showDialog()
                presenter.changeStatusTask(article, 1)
                btnWork.visibility = View.GONE
                btnClanced.visibility = View.VISIBLE
                btnCheck.visibility = if (shouldShowCheckButton()) View.VISIBLE else View.GONE
            }

            btnClanced.setOnClickListener {
                showDialog()
                presenter.sendEndStatus()
            }

            if (shouldShowCheckButton()) {
                srok.visibility = View.VISIBLE
                btnCheck.visibility = View.VISIBLE
            } else {
                srok.visibility = View.GONE
                btnCheck.visibility = View.GONE
                btnNext.visibility = View.VISIBLE
            }

            btnCheck.setOnClickListener { handleCheckButtonClick() }

            lineSyrya.visibility = View.VISIBLE
            binding.artikulSyrya.text = "Артикул сырья: $articulSyrya"
            binding.kolvoSurya.text = "Количество сырья: $kolvoSyrya"

            btnNext.setOnClickListener {
                (activity as StartActivity).replaceFragment(ChooseOpFragment.newInstance(), false)
            }
            btnClanced.setOnClickListener {
                val dialog = CancelReasonDialog.newInstance(this@InfoSyryoFragment)
                dialog.isCancelable = true
                requireActivity().supportFragmentManager.let { dialog.show(it, "cancel_reason") }
            }
        }
    }

    private fun shouldShowCheckButton(): Boolean {
        val prefics = SPHelper.getPrefics()
        return (SPHelper.getSrokGodnosti() && prefics == "WB") || prefics in listOf("OZON", "ЯМ")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleCheckButtonClick() {
        with(binding) {
            when {
                first.text.isNotEmpty() && second.text.isNotEmpty() -> {
                    if (areDatesValid(first.text.toString(), second.text.toString())) {
                        firstAndLastDate(first.text.toString(), second.text.toString())
                    } else {
                        showInvalidDateFormatError()
                    }
                }
                first.text.isNotEmpty() && month.text.isNotEmpty() -> {
                    if (isValidDate(first.text.toString())) {
                        calculateExpirationDate(first.text.toString(), month.text.toString().toInt())
                    } else {
                        showInvalidDateFormatError()
                    }
                }
                first.text.isNotEmpty() -> {
                    if (isValidDate(first.text.toString())) {
                        showDialog()
                        presenter.searchArticleInDbForSG(article)
                    } else {
                        showInvalidDateFormatError()
                    }
                }
                second.text.isNotEmpty() -> {
                    if (isValidDate(second.text.toString())) {
                        lastDate()
                    } else {
                        showInvalidDateFormatError()
                    }
                }
                else -> {
                    showToast("Введите дату срока годности")
                }
            }
        }
    }

    private fun areDatesValid(vararg dates: String): Boolean {
        return dates.all { isValidDate(it) }
    }

    private fun isValidDate(date: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply { isLenient = false }
            dateFormat.parse(date) != null
        } catch (e: ParseException) {
            false
        }
    }

    private fun showInvalidDateFormatError() {
        showToast("Неверный формат даты. Используйте формат dd.MM.yyyy")
    }

    private fun lastDate() {
        date = binding.second.text.toString()
        persent = "0"
        visableDialogApprove(date)
    }

    private fun firstAndLastDate(first: String, second: String) {
        visableDialogApprove(checkDate(first, second))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateExpirationDate(firstDateInput: String, months: Int) {
        val expirationDate = LocalDate.parse(firstDateInput, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            .plusMonths(months.toLong())

        val secondDate = expirationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        visableDialogApprove(checkDate(firstDateInput, secondDate))
    }

    private fun checkDate(first: String, second: String): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val firstDate: Date? = dateFormat.parse(first)
        val secondDate: Date? = dateFormat.parse(second)
        date = second
        val currentDate = Date()

        val diffDays = (secondDate!!.time - firstDate!!.time) / (1000 * 60 * 60 * 24)
        val pastInDays = (currentDate.time - firstDate.time) / (1000 * 60 * 60 * 24)

        val percentage = pastInDays.toDouble() / diffDays * 100
        return String.format("%.2f%%", percentage)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun getDataInfo(data: ArticlesResponse.Articuls) {
        // TODO: Implement this method
    }

    override fun errorMessage(msg: String) {
        mWaitDialog.dismiss()
        showToast(msg)
    }

    override fun successFindShk(msg: String) {
        mWaitDialog.dismiss()
        showToast(msg)
    }

    override fun success() {
        (activity as StartActivity).replaceFragment(ChooseOpFragment.newInstance(), false)
    }

    override fun createNewShk(shk: String) {
        Log.d("NENNENENENE", "DJKDJKFJKL")
        val dialog = ApproveShkDialog.newInstance(shk, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, "gggg") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun checkLastPeriodDate(days: Int) {
        mWaitDialog.dismiss()
        visableDialogApprove(getPersent(days))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPersent(days: Int): String {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val startDate = LocalDate.parse(binding.first.text.toString(), formatter)
        val currentDate = LocalDate.now()
        date = startDate.plusDays(days.toLong()).toString()

        val elapsedDays = ChronoUnit.DAYS.between(startDate, currentDate)
        val percentage = (elapsedDays.toDouble() / days.toDouble()) * 100

        return String.format("%.2f%%", percentage)
    }

    private fun visableDialogApprove(text: String) {
        persent = text
        val dialog = ApproveSrokGodnostiDialog.newInstance(text, this)
        dialog.isCancelable = true
        requireActivity().supportFragmentManager.let { dialog.show(it, ApproveSrokGodnostiDialog.TAG) }
    }

    override fun writeLastDate() {
        mWaitDialog.dismiss()
        binding.first.text.clear()
        showToast("Введите последнюю дату")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Установите binding в null, чтобы избежать утечек памяти
    }

    override fun sendApprove(approve: Boolean) {
        if (approve) {
            Log.d("APPROVE", "TRUE")
            presenter.sendSrokGodnosti(date, persent)
        } else {
            Log.d("APPROVE", "FALSE")
            presenter.sendEndStatus()
        }
    }

    override fun successWriteSG() {
        mWaitDialog.dismiss()
        showToast("Срок годности успешно записан")
        with(binding) {
            btnCheck.visibility = View.GONE
            srok.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
        }
    }

    override fun errorWriteSg() {
        mWaitDialog.dismiss()
        showToast("Ошибка соединения, повторите попытку")
    }

    override fun successEndStatus() {
        mWaitDialog.dismiss()
        if(!buffer_two){
            if (buffer) {
                buffer = false
                presenter.sendEndStatus()
            } else {
                (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
            }
        } else Toast.makeText(context, "Ваш комментарий записан, вы можете продолжить выкладку.", Toast.LENGTH_SHORT).show()
    }


    override fun sendNewShk(shk: String?) {
        shk?.let { presenter.updateShk(it) }
    }

    override fun successEndSG() {
        mWaitDialog.dismiss()
        (activity as StartActivity).replaceFragment(TasksFragment.newInstance(SPHelper.getNameTask()), false)
    }

    override fun onReasonSelected(reason: String, comment: String) {
        presenter.cancelTask(reason, comment)
        buffer_two = true
        if(reason == "Убрать из обработки(экстренно)") {
            buffer = true
            buffer_two = false
        }
    }
}
