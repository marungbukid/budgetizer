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

package com.budgetizer.core.entry.data

import com.budgetizer.core.data.Result
import com.budgetizer.core.data.entry.model.Entry
import java.util.Date

class EntryRepository constructor(
    private val localDataSource: EntryLocalDataSource
) {

    suspend fun getEntriesByDate(date: Date): Result<List<Entry>> {
        val localEntries = localDataSource.getEntries(date)
        if (localEntries is Result.Success) return localEntries

        return Result.Error(Exception("Unable to fetch entries"))
    }

    suspend fun getMonthEntriesToDate(date: Date): Result<List<Entry>> {
        val localEntries = localDataSource.getMonthEntriesToDate(date)
        if (localEntries is Result.Success) return localEntries

        return Result.Error(Exception("Unable to fetch entries"))
    }

    suspend fun addEntry(entry: Entry) {
        localDataSource.addEntry(entry)
    }

    suspend fun updateEntry(entry: Entry) {
        localDataSource.updateEntry(entry)
    }

    companion object {
        @Volatile
        private var INSTANCE: EntryRepository? = null

        fun getInstance(
            localDataSource: EntryLocalDataSource
        ): EntryRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EntryRepository(
                    localDataSource
                ).also { INSTANCE = it }
            }
        }
    }
}
