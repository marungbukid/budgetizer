package com.budgetizer.challenge.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.budgetizer.core.data.challenge.model.Challenge
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ChallengeViewModelFactory @Inject constructor(
    private val challenge: Challenge,
    private val prefs: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != ChallengeViewModel::class.java) {
            throw IllegalArgumentException("ViewModel class not found")
        }
        return ChallengeViewModel(
            challenge,
            prefs
        ) as T
    }
}