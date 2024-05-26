package com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race

sealed interface RaceDetailState {
    data object Empty : RaceDetailState
    data class Content(
        val race: Race,
        val athleteList: List<Athlete> = emptyList(),
    ) : RaceDetailState
}
