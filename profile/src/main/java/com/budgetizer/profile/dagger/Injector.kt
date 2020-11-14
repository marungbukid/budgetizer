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

package com.budgetizer.profile.dagger

import com.budgetizer.core.dagger.SharedPreferencesModule
import com.budgetizer.core.profile.data.ProfileLocalDataSource
import com.budgetizer.profile.ui.ProfileActivity
import com.budgetizer.ui.coreComponent

fun inject(into: ProfileActivity) {
    DaggerProfileComponent.builder()
        .sharedPreferencesModule(
            SharedPreferencesModule(
                into,
                ProfileLocalDataSource.PROFILE_PREFS
            )
        )
        .profileModule(ProfileModule(into))
        .coreComponent(into.coreComponent())
        .build()
        .inject(into)
}
