package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory
import com.jaroapps.stopwatchforcompetitions.domain.model.Race

sealed interface FastResultState {
    data object Default : FastResultState
    data class Content(
        val fastResultList: List<FastResultAthleteHistory>,
        val race: Race,
    ) : FastResultState
}