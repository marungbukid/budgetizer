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

package com.budgetizer.core.data.entry

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.budgetizer.core.data.entry.model.Entry

@Dao
interface EntryDao {

    @Query("SELECT * FROM entry")
    suspend fun getAll(): List<Entry>

    @Query("SELECT * FROM entry WHERE strftime('%d-%m-%Y', datetime(created_at/1000, 'unixepoch')) = strftime('%d-%m-%Y', datetime(:date/1000, 'unixepoch'))")
    suspend fun getEntryByDate(date: Long): List<Entry>

    @Query("SELECT * FROM entry WHERE source = :tag ORDER BY created_at ASC")
    suspend fun getEntryByTag(tag: String): List<Entry>

    @Query("SELECT * FROM entry WHERE strftime('%m', datetime(created_at/1000, 'unixepoch')) = strftime('%m', datetime(:date/1000, 'unixepoch')) OR strftime('%m', datetime(updated_at/1000, 'unixepoch')) = strftime('%m', datetime(:date/1000, 'unixepoch'))")
    suspend fun getMonthEntriesToDate(date: Long): List<Entry>

    @Query("SELECT * FROM entry WHERE id = :id")
    suspend fun getEntry(id: Long): Entry

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: Entry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateEntry(entry: Entry): Int

    @Query("DELETE FROM entry WHERE source = :tag")
    suspend fun deleteEntriesByTag(tag: String)
}
