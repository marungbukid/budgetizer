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

package com.budgetizer.profile.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budgetizer.core.profile.data.ProfileRepository
import com.budgetizer.core.profile.data.model.Profile

class ProfileViewModel constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableLiveData<ProfileEvents>()
    val state: LiveData<ProfileEvents>
        get() = _state

    init {
        if (profileRepository.hasProfile) {
            emitState(ProfileEvents.OnProfileFetched(profileRepository.profile!!))
        }
    }

    fun saveProfile(profile: Profile) {
        profileRepository.updateProfile(profile)
    }

    private fun emitState(state: ProfileEvents) {
        _state.value = state
    }
}
