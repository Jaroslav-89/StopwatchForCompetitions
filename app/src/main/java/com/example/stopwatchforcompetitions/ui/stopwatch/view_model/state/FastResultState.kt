package com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state

import com.example.stopwatchforcompetitions.domain.model.Athlete

sealed interface FastResultState {
    data object Default : FastResultState
    data class Content(
        val athletesList: List<Athlete>
    ) : FastResultState
}