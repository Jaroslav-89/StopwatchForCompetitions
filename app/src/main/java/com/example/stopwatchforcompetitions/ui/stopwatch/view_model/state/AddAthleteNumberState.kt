package com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state

sealed interface AddAthleteNumberState {
    data object Default : AddAthleteNumberState
    data class IsStarted(
        val numberOfTextView: Int,
        val valueOfTextViewOne: String,
        val valueOfTextViewTwo: String,
        val valueOfTextViewThree: String,
        val valueOfTextViewFour: String
    ) : AddAthleteNumberState
}


