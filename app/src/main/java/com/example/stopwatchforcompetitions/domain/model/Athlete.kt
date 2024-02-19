package com.example.stopwatchforcompetitions.domain.model

data class Athlete(
    val id: Long,
    val race: Long,
    val number: String,
    val lapsTime: List<Long>,
    val addLastResult: Long,
    val isExpandable: Boolean,
    val position: Int = 0,
)