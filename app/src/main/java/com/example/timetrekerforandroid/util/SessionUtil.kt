package com.komus_lc.upp.util

import android.database.Cursor
import java.text.SimpleDateFormat
import java.util.Date

class SessionUtil {
    fun createDateForSession(): String {
        //Нам нужны даты (дата и дата+время)
        // Преобразуем текущую в дату "dd.mm.yyyy HH:mm:ss"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val bDate = dateFormat.format(Date())

        //todo дальше нужгно отыскать айди-строки
        // либо просто айди последней сессии которую
        // только что добавили это номер сессии

        return bDate
    }

}