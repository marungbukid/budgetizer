package com.budgetizer.core.util

import java.util.Date

fun Date.toFixed(): String {
    return DateFormat.format(this)
}