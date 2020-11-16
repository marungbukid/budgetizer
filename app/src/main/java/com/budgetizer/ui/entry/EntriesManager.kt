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

package com.budgetizer.ui.entry

import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.data.entry.model.EntryRange

class EntriesManager(entries: List<Entry>) {

    private val daily: List<Entry> = entries.filter { it.entryRange == EntryRange.TODAY }
    private val weekly: List<Entry> = entries.filter { it.entryRange == EntryRange.WEEKLY }
    private val halfMonth: List<Entry> = entries.filter { it.entryRange == EntryRange.HALF_MONTH }
    private val monthly: List<Entry> = entries.filter { it.entryRange == EntryRange.MONTHLY }
    private val items = ArrayList<Entry>()
    private val entryItems = ArrayList<EntryListItem>()

    init {
        items.addAll(daily)
        items.addAll(weekly)
        items.addAll(halfMonth)
        items.addAll(monthly)

        if (daily.isNotEmpty()) {
            entryItems.add(EntryRangeItem(daily[0].entryRange))
            entryItems.addAll(daily.map { EntryItem(it) })
        }

        if (weekly.isNotEmpty()) {
            entryItems.add(EntryRangeItem(weekly[0].entryRange))
            entryItems.addAll(weekly.map { EntryItem(it) })
        }

        if (halfMonth.isNotEmpty()) {
            entryItems.add(EntryRangeItem(halfMonth[0].entryRange))
            entryItems.addAll(halfMonth.map { EntryItem(it) })
        }

        if (monthly.isNotEmpty()) {
            entryItems.add(EntryRangeItem(monthly[0].entryRange))
            entryItems.addAll(monthly.map { EntryItem(it) })
        }
    }

    fun getRealEntries(): List<Entry> {
        return items
    }

    fun getEntries(): List<EntryListItem> {
        return entryItems
    }
}
