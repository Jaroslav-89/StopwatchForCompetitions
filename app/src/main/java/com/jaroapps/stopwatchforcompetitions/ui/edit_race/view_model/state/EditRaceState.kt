package com.jaroapps.stopwatchforcompetitions.ui.edit_race.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.Race

sealed interface EditRaceState {
    data object Empty : EditRaceState
    data class Content(
        val race: Race
    ) : EditRaceState
}
