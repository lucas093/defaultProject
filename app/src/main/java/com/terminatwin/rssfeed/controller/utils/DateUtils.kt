package com.terminatwin.rssfeed.controller.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {
    fun format(date: Date?, format: String): String {
        if (date == null)
            return ""

        val formater = SimpleDateFormat(format, Locale.FRENCH)
        return formater.format(date)
    }

    fun getDate(date: String, format: String): Date? {
        val formater = SimpleDateFormat(format, Locale.FRENCH)
        try {
            return formater.parse(date)
        } catch (e: ParseException) {
            return null
        }

    }
}