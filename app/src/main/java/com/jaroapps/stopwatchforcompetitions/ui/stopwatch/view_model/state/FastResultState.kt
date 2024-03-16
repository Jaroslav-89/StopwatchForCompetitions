package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race

sealed interface FastResultState {
    data object Default : FastResultState
    data class Content(
        val athletesList: List<Athlete>,
        val race: Race,
    ) : FastResultState
}