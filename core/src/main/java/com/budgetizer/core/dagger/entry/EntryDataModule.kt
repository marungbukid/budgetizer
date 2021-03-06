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

package com.budgetizer.core.dagger.entry

import androidx.fragment.app.FragmentActivity
import com.budgetizer.core.dagger.scope.FeatureScope
import com.budgetizer.core.data.entry.EntryDao
import com.budgetizer.core.data.AppDatabase
import com.budgetizer.core.entry.data.EntryLocalDataSource
import com.budgetizer.core.entry.data.EntryRepository
import dagger.Module
import dagger.Provides

@Module
class EntryDataModule constructor(private val activity: FragmentActivity) {

    @Provides
    @FeatureScope
    fun provideEntryRepository(
        localDataSource: EntryLocalDataSource
    ): EntryRepository =
        EntryRepository.getInstance(localDataSource)

    @Provides
    @FeatureScope
    fun provideEntryDao(): EntryDao =
        AppDatabase.getInstance(activity).entryDao()
}
