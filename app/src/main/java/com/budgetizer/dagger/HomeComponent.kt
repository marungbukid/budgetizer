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

import com.budgetizer.core.dagger.CoreComponent
import com.budgetizer.core.dagger.SharedPreferencesModule
import com.budgetizer.core.dagger.entry.EntryDataModule
import com.budgetizer.core.dagger.scope.FeatureScope
import com.budgetizer.ui.HomeActivity
import com.budgetizer.ui.entry.EntriesFragment
import com.budgetizer.ui.stats.StatsFragment
import dagger.Component

@Component(
    modules = [HomeModule::class, SharedPreferencesModule::class],
    dependencies = [CoreComponent::class]
)
@FeatureScope
interface HomeComponent {
    @Component.Builder
    interface Builder {
        fun coreComponent(module: CoreComponent): Builder
        fun entryDataModule(module: EntryDataModule): Builder
        fun homeModule(module: HomeModule): Builder
        fun sharedPreferencesModule(module: SharedPreferencesModule): Builder
        fun build(): HomeComponent
    }

    fun inject(into: HomeActivity)
    fun inject(into: EntriesFragment)
    fun inject(into: StatsFragment)
}
