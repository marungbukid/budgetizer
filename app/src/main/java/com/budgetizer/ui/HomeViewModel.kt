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

package com.budgetizer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetizer.core.challenge.data.ChallengeRepository
import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.data.Result
import com.budgetizer.core.data.challenge.model.Challenge
import com.budgetizer.core.entry.data.EntryRepository
import com.budgetizer.core.profile.data.ProfileRepository
import com.budgetizer.core.profile.data.model.Profile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class HomeViewModel constructor(
    private val profileRepository: ProfileRepository,
    private val entryRepository: EntryRepository,
    private val challengeRepository: ChallengeRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    private val _events = MutableLiveData<HomeEvents>()
    val events: LiveData<HomeEvents>
        get() = _events

    val profile: Profile?
        get() = profileRepository.profile

    fun hasProfile() = profileRepository.hasProfile

    fun getMonthEntriesToDate(date: Date) = viewModelScope.launch(dispatcherProvider.computation) {
        val result = entryRepository.getMonthEntriesToDate(date)

        withContext(dispatcherProvider.main) {
            when (result) {
                is Result.Success ->
                    emitUiEvents(HomeEvents.MonthEntriesUpdate(result.data))
                is Result.Error -> {
                }
            }
        }
    }

    fun getChallenges() = viewModelScope.launch(dispatcherProvider.computation) {
        val result = challengeRepository.getChallenges()

        withContext(dispatcherProvider.main) {
            when (result) {
                is Result.Success ->
                    emitUiEvents(HomeEvents.ChallengesFetched(result.data))
                is Result.Error -> {
                }
            }
        }
    }

    fun insertChallenges(items: List<Challenge>) =
        viewModelScope.launch(dispatcherProvider.computation) {
            items.forEach {
                challengeRepository.addChallenge(it)
            }
        }

    private fun emitUiEvents(event: HomeEvents) {
        _events.value = event
    }
}
