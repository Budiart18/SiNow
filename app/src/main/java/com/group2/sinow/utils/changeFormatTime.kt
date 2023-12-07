package com.group2.sinow.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun changeFormatTime(inputDate: String): String {
    val oldFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val date = oldFormat.parse(inputDate)
    val newFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
    return newFormat.format(date ?: "")
}