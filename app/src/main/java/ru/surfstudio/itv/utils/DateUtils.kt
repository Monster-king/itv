package ru.surfstudio.itv.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val fromFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ru","RU"))
    private val toFormat = SimpleDateFormat("dd MMM yyyy", Locale("ru","RU"))

    fun formatDate(date: String): String {
        return toFormat.format(fromFormat.parse(date))
    }
}