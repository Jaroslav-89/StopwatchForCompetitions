package com.jaroapps.stopwatchforcompetitions.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fast_result_history_table")
data class FastResultHistoryEntity(
    val id: Long,
    val race: Long,
    val number: String,
    val currentLapNumber: Int,
    val currentLapTime: Long,
    @PrimaryKey(autoGenerate = false)
    val addLastResult: Long,
)
