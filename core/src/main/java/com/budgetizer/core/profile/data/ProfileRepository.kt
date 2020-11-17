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

package com.budgetizer.core.profile.data

import com.budgetizer.core.profile.data.model.Profile

class ProfileRepository constructor(
    private val localDataSource: ProfileLocalDataSource,
    private val remoteDataSource: ProfileRemoteDataSource
) {

    var profile: Profile? = null
        private set

    val hasProfile: Boolean
        get() = profile != null

    init {
        profile = localDataSource.profile
    }

    fun updateProfile(profile: Profile) {
        // TODO: Change if profile can be retrieved remotely
        this.profile = profile
        localDataSource.profile = profile
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(
            localDataSource: ProfileLocalDataSource,
            remoteDataSource: ProfileRemoteDataSource
        ): ProfileRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(
                    localDataSource,
                    remoteDataSource
                ).also { INSTANCE = it }
            }
        }
    }
}
