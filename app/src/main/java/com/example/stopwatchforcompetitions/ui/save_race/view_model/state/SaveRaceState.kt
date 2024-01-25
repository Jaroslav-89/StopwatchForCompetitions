package com.example.stopwatchforcompetitions.ui.save_race.view_model.state

import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race

sealed interface SaveRaceState {
    data object Empty : SaveRaceState
    data class Content(
        val race: Race? = null,
        val athleteList: List<Athlete> = emptyList()
    ) : SaveRaceState
}
