package com.budgetizer.core.util

import java.util.Calendar
import java.util.Date

fun Date.isSameDay(to: Date): Boolean {
    val c1 = Calendar.getInstance().also { it.time = this }
    val c2 = Calendar.getInstance().also { it.time = to }
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
        && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
        && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
}

fun Date.isSameYear(to: Date): Boolean {
    val c1 = Calendar.getInstance().also { it.time = this }
    val c2 = Calendar.getInstance().also { it.time = to }
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
        && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
}