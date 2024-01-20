package com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state

sealed interface TimerState {
    data object Default : TimerState
    data class IsStarted(
        val time: String
    ) : TimerState
}