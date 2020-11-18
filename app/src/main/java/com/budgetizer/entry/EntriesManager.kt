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

package com.budgetizer.entry

import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.util.isSameDay
import com.budgetizer.core.util.isSameYear
import com.budgetizer.ui.entry.EntryItem
import com.budgetizer.ui.entry.EntryListItem
import com.budgetizer.ui.entry.EntryRangeItem
import java.util.Date

class EntriesManager(entries: List<Entry>, date: Date = Date()) {

    private val daily: List<Entry> =
        entries.filter { it.entryRange == EntryRange.TODAY }
    private val weekly: List<Entry> = entries.filter { it.entryRange == EntryRange.WEEKLY }
    private val halfMonth: List<Entry> = entries.filter { it.entryRange == EntryRange.HALF_MONTH }
    private val monthly: List<Entry> = entries.filter { it.entryRange == EntryRange.MONTHLY }
    private val items = ArrayList<Entry>()
    private val entryItems = ArrayList<EntryListItem>()

    init {
        val tempDaily = daily.filter {
            it.updatedAt.isSameDay(date)
        }
        items.addAll(tempDaily)
        items.addAll(weekly)
        items.addAll(halfMonth)
        items.addAll(monthly)

        if (tempDaily.isNotEmpty()) {
            entryItems.add(EntryRangeItem(tempDaily[0].entryRange))
            entryItems.addAll(tempDaily.map { EntryItem(it) })
        }

        if (weekly.isNotEmpty()) {
            entryItems.add(EntryRangeItem(weekly[0].entryRange))
            entryItems.addAll(weekly.filter { it.createdAt.isSameYear(date) }.map { EntryItem(it) })
        }

        if (halfMonth.isNotEmpty()) {
            entryItems.add(EntryRangeItem(halfMonth[0].entryRange))
            entryItems.addAll(halfMonth.filter { it.createdAt.isSameYear(date) }
                .map { EntryItem(it) })
        }

        if (monthly.isNotEmpty()) {
            entryItems.add(EntryRangeItem(monthly[0].entryRange))
            entryItems.addAll(monthly.filter { it.createdAt.isSameYear(date) }
                .map { EntryItem(it) })
        }
    }

    fun getEntries(): List<EntryListItem> {
        return entryItems
    }

    fun getMonthlyEntries(): List<EntryHolder> {
        return listOf(
            EntryHolder(
                range = EntryRange.TODAY,
                items = daily
            ),
            EntryHolder(
                range = EntryRange.WEEKLY,
                items = weekly
            ),
            EntryHolder(
                range = EntryRange.HALF_MONTH,
                items = halfMonth
            ),
            EntryHolder(
                range = EntryRange.MONTHLY,
                items = monthly
            )
        )
    }
}
