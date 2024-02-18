package com.example.stopwatchforcompetitions.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "race_table")
data class RaceEntity(
    @PrimaryKey(autoGenerate = false)
    val startTime: Long,
    val name: String,
    val description: String,
    val imgUrl: String,
    val lapDistance: Int,
    val totalLapsInRace: Int,
    val athletes: String,
    val isStarted: Boolean,
    val isFavorite: Boolean,
)
