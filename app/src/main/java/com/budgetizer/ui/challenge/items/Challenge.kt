package com.budgetizer.ui.challenge.items

import com.budgetizer.R
import com.budgetizer.core.data.challenge.model.Challenge

object Challenge {

    val preInsert = listOf(
        Challenge(
            id = 1,
            label = "52 Week",
            thumbRes = R.drawable.thumb_52_week,
            isActive = false,
            isFinished = false,
            navId = R.id.fiftyTwoWeekFragment
        )
    )
}