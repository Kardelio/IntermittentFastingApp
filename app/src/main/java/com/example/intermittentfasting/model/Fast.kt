package com.example.intermittentfasting.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fasts")
data class Fast(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTimeUTC: String,
    val endTimeUTC: String,
    val manuallyEnteredPastFast: Boolean = false,
    val startTimestamp: Long = 0L,
    val endTimestamp: Long = 0L,
    @ColumnInfo(name = "targetHours", defaultValue = "16")
    val targetHours: Int = 16
) {
    fun isActive(): Boolean {
        return endTimeUTC.isBlank()
    }
}

