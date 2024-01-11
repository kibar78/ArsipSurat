package com.example.arsipsurat.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateFormat {
    private const val usualFormat = "yyyy-MM-dd HH:mm:ss"

    fun format(aDate: String, aFormat: String): String {
        if (aDate.isEmpty()) return ""
        try {
            val date = SimpleDateFormat(usualFormat, Locale.getDefault()).parse(aDate)
            if (date != null)
                return SimpleDateFormat(aFormat, Locale.getDefault()).format(date)
            else
                return ""
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //String timezoneID = TimeZone.getDefault().getID();
        //formatter.setTimeZone(TimeZone.getTimeZone(timezoneID));
        return ""
    }
}