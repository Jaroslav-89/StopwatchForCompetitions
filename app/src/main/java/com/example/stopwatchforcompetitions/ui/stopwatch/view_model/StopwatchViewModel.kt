package com.example.stopwatchforcompetitions.ui.stopwatch.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stopwatchforcompetitions.ui.stopwatch.fragment.StopwatchFragment.Companion.DELETE

class StopwatchViewModel : ViewModel() {

    private var numberOfTextView = 1
    private var valueOfTextViewOne = ""
    private var valueOfTextViewTwo = ""
    private var valueOfTextViewThree = ""
    private var valueOfTextViewFour = ""

//    private val _timerState = MutableLiveData<>()
//    val timerState: LiveData<>
//        get() = _timerState
//
//    private val _fastResultState = MutableLiveData<>()
//    val fastResultState: LiveData<>
//        get() = _fastResultState

    private val _addAthleteNumberState = MutableLiveData<AddAthleteNumberState>()
    val addAthleteNumberState: LiveData<AddAthleteNumberState>
        get() = _addAthleteNumberState

    init {
        renderAddAthleteNumberState()
    }

    fun changeTextViewFocus(newNumberOfTv: Int) {
        numberOfTextView = newNumberOfTv
        renderAddAthleteNumberState()
    }

    fun changeTextViewText(char: String) {
        if (char == DELETE) {
            when (numberOfTextView) {
                1 -> valueOfTextViewOne = valueOfTextViewOne.dropLast(1)
                2 -> valueOfTextViewTwo = valueOfTextViewTwo.dropLast(1)
                3 -> valueOfTextViewThree = valueOfTextViewThree.dropLast(1)
                4 -> valueOfTextViewFour = valueOfTextViewFour.dropLast(1)
            }
        } else {
            when (numberOfTextView) {
                1 -> valueOfTextViewOne += char
                2 -> valueOfTextViewTwo += char
                3 -> valueOfTextViewThree += char
                4 -> valueOfTextViewFour += char
            }
        }
        renderAddAthleteNumberState()
    }

    private fun renderAddAthleteNumberState() {
        _addAthleteNumberState.postValue(
            AddAthleteNumberState(
                numberOfTextView,
                valueOfTextViewOne,
                valueOfTextViewTwo,
                valueOfTextViewThree,
                valueOfTextViewFour
            )
        )
    }

}