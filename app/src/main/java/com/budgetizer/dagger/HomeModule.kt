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

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.budgetizer.core.dagger.DataManagerModule
import com.budgetizer.core.dagger.challenge.ChallengeDataModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.ui.HomeViewModel
import com.budgetizer.ui.HomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        DataManagerModule::class,
        EntryDataModule::class,
        ChallengeDataModule::class
    ]
)
class HomeModule(private val activity: FragmentActivity) {

    @Provides
    fun provideHomeViewModel(
        factory: HomeViewModelFactory
    ): HomeViewModel =
        ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
}
