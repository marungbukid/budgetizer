/*
 * Copyright 2020 Jan Maru De Guzman.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.budgetizer.dagger

import com.budgetizer.core.dagger.SharedPreferencesModule
import com.budgetizer.core.dagger.challenge.ChallengeDataModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.core.profile.data.ProfileLocalDataSource
import com.budgetizer.ui.HomeActivity
import com.budgetizer.ui.challenge.ChallengeFragment
import com.budgetizer.ui.coreComponent
import com.budgetizer.ui.entry.EntriesFragment
import com.budgetizer.ui.stats.StatsFragment

fun inject(into: HomeActivity) {
    DaggerHomeComponent.builder()
        .coreComponent(into.coreComponent())
        .homeModule(HomeModule(into))
        .entryDataModule(EntryDataModule(into))
        .challengeDataModule(ChallengeDataModule(into))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into,
                ProfileLocalDataSource.PROFILE_PREFS
            )
        )
        .build()
        .inject(into)
}

fun inject(into: EntriesFragment) {
    DaggerHomeComponent.builder()
        .coreComponent(into.requireActivity().coreComponent())
        .homeModule(HomeModule(into.requireActivity()))
        .entryDataModule(EntryDataModule(into.requireActivity()))
        .challengeDataModule(ChallengeDataModule(into.requireActivity()))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into.requireContext(),
                ProfileLocalDataSource.PROFILE_PREFS
            )
        )
        .build()
        .inject(into)
}

fun inject(into: StatsFragment) {
    DaggerHomeComponent.builder()
        .coreComponent(into.requireActivity().coreComponent())
        .homeModule(HomeModule(into.requireActivity()))
        .entryDataModule(EntryDataModule(into.requireActivity()))
        .challengeDataModule(ChallengeDataModule(into.requireActivity()))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into.requireContext(),
                ProfileLocalDataSource.PROFILE_PREFS
            )
        )
        .build()
        .inject(into)
}

fun inject(into: ChallengeFragment) {
    DaggerHomeComponent.builder()
        .coreComponent(into.requireActivity().coreComponent())
        .homeModule(HomeModule(into.requireActivity()))
        .entryDataModule(EntryDataModule(into.requireActivity()))
        .challengeDataModule(ChallengeDataModule(into.requireActivity()))
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into.requireContext(),
                ProfileLocalDataSource.PROFILE_PREFS
            )
        )
        .build()
        .inject(into)
}
