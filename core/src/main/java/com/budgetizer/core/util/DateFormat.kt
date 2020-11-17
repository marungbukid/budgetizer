/*
 * Copyright 2020 Jan Maru De Guzman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.budgetizer.core.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormat {
    @SuppressLint("ConstantLocale")
    private val dateFormat =
        SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    @SuppressLint("ConstantLocale")
    private val dateFormatMonthOnly =
        SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    fun format(date: Date, pattern: String? = null): String {
        if (pattern != null) {
            dateFormat.applyPattern(pattern)
        }
        return dateFormat.format(date.time)
    }
    fun formatMonthOnly(date: Date): String {
        return dateFormatMonthOnly.format(date.time)
    }
}
