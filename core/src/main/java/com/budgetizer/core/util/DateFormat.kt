package com.budgetizer.core.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    @SuppressLint("ConstantLocale")
    private val dateFormat =
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    fun format(date: Date): String {
        return dateFormat.format(date.time)
    }
}