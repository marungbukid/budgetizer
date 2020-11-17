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

package com.budgetizer.entry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.data.entry.model.Entry
import com.budgetizer.core.entry.data.EntryRepository
import kotlinx.coroutines.launch

class EntryViewModel constructor(
    private val entryRepository: EntryRepository,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) : ViewModel() {

    fun addEntry(entry: Entry) = viewModelScope.launch(dispatcherProvider.computation) {
        entryRepository.addEntry(entry)
    }

    fun updateEntry(entry: Entry) = viewModelScope.launch(dispatcherProvider.computation) {
        entryRepository.updateEntry(entry)
    }
}