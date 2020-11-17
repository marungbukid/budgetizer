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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.budgetizer.core.data.DateConverter
import com.budgetizer.core.data.EntryFieldsConverters
import com.budgetizer.core.data.StringListConverter
import com.budgetizer.core.data.entry.model.Entry

@Database(
    entities = [
        Entry::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    EntryFieldsConverters::class,
    StringListConverter::class,
    DateConverter::class
)
abstract class EntryDatabase : RoomDatabase() {

    abstract fun entryDao(): EntryDao

    companion object {
        private const val DATABASE_NAME = "budgetizer-db"

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE entry ADD COLUMN entry_range INTEGER NOT NULL DEFAULT 1")
            }
        }

        // For Singleton instantiation
        @Volatile
        private var INSTANCE: EntryDatabase? = null

        fun getInstance(context: Context): EntryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(
                    context
                ).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): EntryDatabase {
            return Room.databaseBuilder(
                context,
                EntryDatabase::class.java,
                DATABASE_NAME
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}
