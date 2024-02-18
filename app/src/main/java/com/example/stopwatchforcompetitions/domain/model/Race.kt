package com.example.stopwatchforcompetitions.domain.model

data class Race(
    val startTime: Long = 0L,
    val name: String = "",
    val description: String = "",
    val imgUrl: String = "",
    val lapDistance: Int = 0,
    val totalLapsInRace: Int = 0,
    val athletes: List<String> = emptyList(),
    val isStarted: Boolean = false,
    val isFavorite: Boolean = false,
)