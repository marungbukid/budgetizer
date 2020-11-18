package com.budgetizer.challenge.ui

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budgetizer.core.challenge.data.ChallengeLocalDataSource
import com.budgetizer.core.data.challenge.model.Challenge

class ChallengeViewModel constructor(
    private val challenge: Challenge,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _events = MutableLiveData<ChallengeEvents>()
    val events: LiveData<ChallengeEvents>
        get() = _events

    init {
        val shouldIntro =
            prefs.getBoolean(ChallengeLocalDataSource.KEY_SHOULD_INTRO + "$challenge.id", true)

        if (shouldIntro) {
            emitState(ChallengeEvents.OnBoard(challenge))
        }
    }

    private fun emitState(event: ChallengeEvents) {
        _events.value = event
    }

    fun activate() {
        prefs.edit {
            putBoolean(ChallengeLocalDataSource.KEY_SHOULD_INTRO + "$challenge.id", false)
        }
    }

}