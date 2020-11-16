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