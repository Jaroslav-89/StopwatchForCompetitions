package com.jaroapps.stopwatchforcompetitions.domain.model

data class FastResultAthleteHistory(
    val id: Long,
    val race: Long,
    val number: String,
    val currentLapNumber: Int,
    val currentLapTime: Long,
    val addLastResult: Long,
)