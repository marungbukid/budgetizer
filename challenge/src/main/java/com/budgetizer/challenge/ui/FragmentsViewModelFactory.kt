package com.budgetizer.challenge.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.entry.data.EntryRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FragmentsViewModelFactory @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val entryRepository: EntryRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != FragmentsViewModel::class.java) {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
        return FragmentsViewModel(
            sharedPreferences,
            entryRepository,
            dispatcherProvider
        ) as T
    }
}