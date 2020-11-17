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

package com.budgetizer.core.dagger.profile

import com.budgetizer.core.dagger.scope.FeatureScope
import com.budgetizer.core.profile.data.ProfileLocalDataSource
import com.budgetizer.core.profile.data.ProfileRemoteDataSource
import com.budgetizer.core.profile.data.ProfileRepository
import dagger.Module
import dagger.Provides

@Module
class ProfileDataModule {

    @Provides
    @FeatureScope
    fun provideProfileRepository(
        localDataSource: ProfileLocalDataSource,
        remoteDataSource: ProfileRemoteDataSource
    ): ProfileRepository =
        ProfileRepository.getInstance(localDataSource, remoteDataSource)
}
