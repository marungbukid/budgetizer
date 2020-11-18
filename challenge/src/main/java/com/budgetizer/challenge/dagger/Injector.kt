package com.budgetizer.challenge.dagger

import com.budgetizer.challenge.ui.ChallengeActivity
import com.budgetizer.challenge.ui.fiftytwo.FiftyTwoWeekFragment
import com.budgetizer.core.challenge.data.ChallengeLocalDataSource
import com.budgetizer.core.dagger.SharedPreferencesModule
import com.budgetizer.core.dagger.challenge.ChallengeDataModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.ui.coreComponent

fun inject(into: ChallengeActivity, challenge: Challenge) {
    DaggerChallengeComponent.builder()
        .challengeDataModule(ChallengeDataModule(into))
        .challengeInstance(challenge)
        .coreComponent(into.coreComponent())
        .entryDataModule(EntryDataModule(into))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into,
                ChallengeLocalDataSource.CHALLENGE_PREFS
            )
        )
        .challengeModule(ChallengeModule(into))
        .build()
        .inject(into)
}

fun inject(into: FiftyTwoWeekFragment) {
    DaggerFragmentsComponent.builder()
        .challengeDataModule(ChallengeDataModule(into.requireActivity()))
        .coreComponent(into.requireActivity().coreComponent())
        .entryDataModule(EntryDataModule(into.requireActivity()))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into.requireContext(),
                ChallengeLocalDataSource.CHALLENGE_PREFS
            )
        )
        .challengeModule(ChallengeModule(into.requireActivity()))
        .build()
        .inject(into)
}