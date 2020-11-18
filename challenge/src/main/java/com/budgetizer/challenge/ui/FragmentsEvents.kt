package com.budgetizer.challenge.ui

import com.budgetizer.core.data.entry.model.Entry

sealed class FragmentsEvents {
    object EntryUpdated: FragmentsEvents()
    data class IncrementsUpdate(val increments: Double) : FragmentsEvents()
    data class EntriesFetched(val items: List<Entry>) : FragmentsEvents()
}