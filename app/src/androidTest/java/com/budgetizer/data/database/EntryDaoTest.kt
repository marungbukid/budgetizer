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

package com.budgetizer.data.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.budgetizer.core.data.Entry
import com.budgetizer.core.data.EntryType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class EntryDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var entryDb: EntryDatabase
    private lateinit var entryDao: EntryDao

    private val entries = arrayListOf(
        Entry(
            id = 1,
            type = EntryType.EXPENSE,
            label = "Food",
            tag = "food"
        ),
        Entry(
            id = 2,
            type = EntryType.EXPENSE,
            label = "Clothing",
            tag = "util"
        ),
        Entry(
            id = 3,
            type = EntryType.EXPENSE,
            label = "Something",
            tag = "util"
        ),
        Entry(
            id = 4,
            type = EntryType.EXPENSE,
            label = "Test",
            tag = "test"
        )
    )

    @Before
    fun setup() {
        entryDb = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            EntryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
            .also {
                entryDao = it.entryDao()
            }
    }

    @After
    fun teardown() {
        entryDb.close()
    }

    @Test
    fun verify_entryIncomeAdd() = runBlockingTest {
        val entry = Entry(
            id = 5,
            type = EntryType.INCOME,
            label = "Food",
            tag = "food"
        )

        entries.forEach { entryDao.insertEntry(it) }
        entryDao.insertEntry(entry)

        val getEntry = entryDao.getEntry(entry.id)

        Assert.assertEquals("Entry add result:", entry.id, getEntry.id)
    }

    @Test
    fun verify_entryNotFound() = runBlockingTest {
        val getEntry = entryDao.getEntry(10)

        Assert.assertNull("Entry is not found", getEntry)
    }
}
