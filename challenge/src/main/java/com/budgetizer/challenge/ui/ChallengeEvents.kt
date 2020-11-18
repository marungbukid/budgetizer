package com.budgetizer.challenge.ui

import com.budgetizer.core.data.challenge.model.Challenge

sealed class ChallengeEvents {
    data class OnBoard(val challenge: Challenge) : ChallengeEvents()
}