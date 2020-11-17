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

package com.budgetizer.entry.dagger

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.entry.ui.EntryViewModel
import com.budgetizer.entry.ui.EntryViewModelFactory
import dagger.Module
import dagger.Provides

@Module(includes = [EntryDataModule::class])
class EntryModule(private val activity: FragmentActivity) {

    @Provides
    fun provideEntryViewModel(
        factory: EntryViewModelFactory
    ): EntryViewModel =
        ViewModelProvider(activity, factory).get(EntryViewModel::class.java)
}
