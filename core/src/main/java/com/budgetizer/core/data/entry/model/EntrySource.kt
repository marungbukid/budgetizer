package com.budgetizer.core.data.entry.model

object EntrySource {
    const val USER = "USER"

    private const val CHALLENGE = "CHALLENGE"
    fun challengeSource(tag: String): String {
        return "$CHALLENGE#$tag"
    }
}