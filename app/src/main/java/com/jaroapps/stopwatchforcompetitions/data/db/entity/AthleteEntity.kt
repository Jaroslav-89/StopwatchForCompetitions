package com.jaroapps.stopwatchforcompetitions.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athlete_table")
data class AthleteEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val race: Long,
    val number: String,
    val lapsTime: String,
    val addLastResult: Long
)
