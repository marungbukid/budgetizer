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

package com.budgetizer.core.data

import androidx.room.TypeConverter
import com.budgetizer.core.data.entry.model.EntryRange
import com.budgetizer.core.data.entry.model.EntryType

class EntryFieldsConverters {

    @TypeConverter
    fun entryTypeToInt(entryType: EntryType): Int {
        return entryType.ordinal
    }

    @TypeConverter
    fun intToEntryType(intEntryType: Int): EntryType {
        return enumValues<EntryType>()[intEntryType]
    }

    @TypeConverter
    fun entryRangeToInt(entryRange: EntryRange): Int {
        return entryRange.ordinal
    }

    @TypeConverter
    fun intToEntryRange(intEntryRange: Int): EntryRange {
        return enumValues<EntryRange>()[intEntryRange]
    }
}
