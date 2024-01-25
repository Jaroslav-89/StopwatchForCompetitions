package com.example.stopwatchforcompetitions.domain.model

data class Race(
    val startTime: Long,
    val name: String,
    val description: String,
    val imgUrl: String,
    val lapDistance: Int,
    val athletes: List<String>,
    val isStarted: Boolean
)