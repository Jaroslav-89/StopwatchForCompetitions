package com.jaroapps.stopwatchforcompetitions.ui.all_races.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.Race

sealed interface AllRaceState {
    data object Empty : AllRaceState
    data class Content(
        val raceList: List<Race>
    ) : AllRaceState
}
