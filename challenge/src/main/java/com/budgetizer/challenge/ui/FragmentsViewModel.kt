package com.budgetizer.challenge.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetizer.core.challenge.data.ChallengeLocalDataSource
import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.data.Result
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.entry.data.EntryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentsViewModel constructor(
    private val prefs: SharedPreferences,
    private val entryRepository: EntryRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _events = MutableLiveData<FragmentsEvents>()
    val events: LiveData<FragmentsEvents>
        get() = _events

    init {
        prefs.getFloat(ChallengeLocalDataSource.KEY_INCREMENTS, -1f).let {
            if (it > 0) emitState(FragmentsEvents.IncrementsUpdate(it.toDouble()))
        }
    }

    fun save(key: String, increments: Double) {
        prefs.edit {
            putFloat(key, increments.toFloat())
        }
    }

    fun getAllEntries(tag: String) = viewModelScope.launch(dispatcherProvider.computation) {
        val result = entryRepository.getEntriesByTag(tag)

        withContext(dispatcherProvider.main) {
            when (result) {
                is Result.Success ->
                    emitState(FragmentsEvents.EntriesFetched(result.data))
                is Result.Error -> {}
            }
        }
    }

    fun updateEntry(entry: Entry) = viewModelScope.launch(dispatcherProvider.computation) {
        val result = entryRepository.updateEntry(entry)

        withContext(dispatcherProvider.main) {
            when (result) {
                is Result.Success ->
                    emitState(FragmentsEvents.EntryUpdated)
                is Result.Error -> {}
            }
        }
    }

    fun addAllEntries(items: List<Entry>) = viewModelScope.launch(dispatcherProvider.computation) {
        for (item in items) {
            entryRepository.addEntry(item)
        }

        withContext(dispatcherProvider.main) {
            emitState(FragmentsEvents.EntryUpdated)
        }
    }

    fun deleteChallengeEntries(challengeTag: String) =
        viewModelScope.launch(dispatcherProvider.computation) {
            entryRepository.deleteAllByTag(challengeTag)
        }

    private fun emitState(events: FragmentsEvents) {
        _events.value = events
    }
}