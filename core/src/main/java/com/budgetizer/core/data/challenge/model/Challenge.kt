package com.budgetizer.core.data.challenge.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "challenge")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "label")
    val label: String,

    @DrawableRes
    @ColumnInfo(name = "thumb_res")
    val thumbRes: Int,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,

    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean,

    @ColumnInfo(name = "nav_id")
    val navId: Int
) : Parcelable