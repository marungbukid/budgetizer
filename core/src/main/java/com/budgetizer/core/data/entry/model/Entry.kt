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

package com.budgetizer.core.data.entry.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "entry")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "type")
    val type: EntryType,

    @ColumnInfo(name = "Label")
    val label: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "tags")
    val tags: List<String>,

    @ColumnInfo(name = "entry_range")
    var entryRange: EntryRange,

    @ColumnInfo(name = "created_at")
    val createdAt: Date,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Date,
) : Parcelable {

    fun calculatedAmount(): Double {
        return when (entryRange) {
            EntryRange.WEEKLY -> amount * 4
            EntryRange.HALF_MONTH -> amount * 2
            EntryRange.TODAY,
            EntryRange.MONTHLY -> amount * 1
        }
    }
}
