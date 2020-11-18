package com.budgetizer.ui.challenge

import com.budgetizer.core.data.challenge.model.Challenge

data class ChallengeHolder(
    val type: ChallengeType,
    val items: List<Challenge>
)

enum class ChallengeType {
    ACTIVE, AVAILABLE, FINISHED
}