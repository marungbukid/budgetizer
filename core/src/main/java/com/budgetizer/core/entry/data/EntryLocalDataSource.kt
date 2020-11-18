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

import com.budgetizer.core.data.CoroutinesDispatcherProvider
import com.budgetizer.core.data.Result
import com.budgetizer.core.data.entry.EntryDao
import com.budgetizer.core.data.entry.model.Entry
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.withContext

class EntryLocalDataSource @Inject constructor(
    private val entryDao: EntryDao,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {

    suspend fun getEntries(date: Date): Result<List<Entry>> = withContext(dispatcherProvider.io) {
        return@withContext try {
            Result.Success(entryDao.getEntryByDate(date.time))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getEntriesByTag(tag: String): Result<List<Entry>> = withContext(dispatcherProvider.io) {
        return@withContext try {
            Result.Success(entryDao.getEntryByTag(tag))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun getMonthEntriesToDate(date: Date): Result<List<Entry>> = withContext(dispatcherProvider.io) {
        return@withContext try {
            Result.Success(entryDao.getMonthEntriesToDate(date.time))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addEntry(entry: Entry) = withContext(dispatcherProvider.io) {
        entryDao.insertEntry(entry)
    }

    suspend fun updateEntry(entry: Entry): Result<Int> = withContext(dispatcherProvider.io) {
        return@withContext try {
            Result.Success(entryDao.updateEntry(entry))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteEntryByTag(tag: String) = withContext(dispatcherProvider.io) {
        entryDao.deleteEntriesByTag(tag)
    }
}
