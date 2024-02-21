package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.ui.stopwatch.fragment.StopwatchFragment
import com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model.state.AddAthleteNumberState
import com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model.state.FastResultState
import com.jaroapps.stopwatchforcompetitions.ui.stopwatch.view_model.state.TimerState
import com.jaroapps.stopwatchforcompetitions.util.Util
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StopwatchViewModel(private val interactor: StopwatchInteractor) : ViewModel() {

    private var timerJob: Job? = null
    private var isStarted = false
    private var startRaceData = 0L
    private var updateAthletesInformationJob: Job? = null

    private var numberOfTextView = StopwatchFragment.FIRST_EDIT_TEXT
    private var valueOfTextViewOne = ""
    private var valueOfTextViewTwo = ""
    private var valueOfTextViewThree = ""
    private var valueOfTextViewFour = ""

    private var currentRace = Race()

    private var athletesListInCurrentRace = emptyList<Athlete>()

    private val _timerState = MutableLiveData<TimerState>(TimerState.Default)
    val timerState: LiveData<TimerState>
        get() = _timerState

    private val _fastResultState = MutableLiveData<FastResultState>(FastResultState.Default)
    val fastResultState: LiveData<FastResultState>
        get() = _fastResultState

    private val _addAthleteNumberState = MutableLiveData<AddAthleteNumberState>(
        AddAthleteNumberState.Default
    )
    val addAthleteNumberState: LiveData<AddAthleteNumberState>
        get() = _addAthleteNumberState

    fun checkRaceHasBeenStarted() {
        if (isStarted) {
            updateRaceInformation()
        } else {
            viewModelScope.launch {
                val race = interactor.checkRaceIsStarted()
                if (race != null) {
                    isStarted = true
                    startRaceData = race.startTime
                    currentRace = race
                    updateRaceInformation()
                    renderAddAthleteNumberState()
                }
            }
        }
    }

    private fun updateRaceInformation() {
        viewModelScope.launch {
            val race = interactor.getRaceInformation(startRaceData)
            if (race.isStarted) {
                currentRace = race
                updateTimer()
                updateAthletesInformation(race)
            }
        }
    }

    private fun updateTimer() {
        if (timerJob == null) {
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(UPDATE_DELAY)
                    renderTimer(
                        TimerState.IsStarted(
                            time =
                            Util.getTimeFormat(
                                System.currentTimeMillis() - currentRace.startTime
                            )
                        )
                    )
                }
            }
        } else {
            if (!isStarted) {
                timerJob?.cancel()
                timerJob = null
                renderTimer(TimerState.Default)
            }
        }
    }

    private fun updateAthletesInformation(race: Race) {
        if (isStarted) {
            if (updateAthletesInformationJob == null) {
                updateAthletesInformationJob = viewModelScope.launch {
                    interactor.getAllAthletesInRace(race.startTime).collect() {
                        if (athletesListInCurrentRace != it) {
                            athletesListInCurrentRace = it
                        }
                        renderAthletesFastResult(athletesListInCurrentRace)
                    }
                }
            } else renderAthletesFastResult(athletesListInCurrentRace)
        } else {
            updateAthletesInformationJob?.cancel()
            updateAthletesInformationJob = null
            _fastResultState.value = FastResultState.Default
        }
    }

    fun onPlayStopButtonClicked() {
        if (isStarted) {
            stop()
        } else {
            start()
        }
    }

    private fun start() {
        viewModelScope.launch {
            startRaceData = System.currentTimeMillis()
            interactor.updateRace(
                Race(
                    startTime = startRaceData,
                    isStarted = true,
                )
            )
            isStarted = true
            updateRaceInformation()
            renderAddAthleteNumberState()
        }
    }

    private fun stop() {
        viewModelScope.launch {
            interactor.stopRace()
        }
        isStarted = false
        currentRace = Race()
        athletesListInCurrentRace = emptyList<Athlete>()
        renderAddAthleteNumberState()
        updateTimer()
        updateAthletesInformation(currentRace)
    }

    fun changeTextViewFocus(newNumberOfTv: Int) {
        if (isStarted) {
            numberOfTextView = newNumberOfTv
            renderAddAthleteNumberState()
        }
    }

    fun changeTextViewText(char: String) {
        if (isStarted) {
            if (char == StopwatchFragment.DELETE) {
                when (numberOfTextView) {
                    StopwatchFragment.FIRST_EDIT_TEXT -> valueOfTextViewOne =
                        valueOfTextViewOne.dropLast(1)

                    StopwatchFragment.SECOND_EDIT_TEXT -> valueOfTextViewTwo =
                        valueOfTextViewTwo.dropLast(1)

                    StopwatchFragment.THIRD_EDIT_TEXT -> valueOfTextViewThree =
                        valueOfTextViewThree.dropLast(1)

                    StopwatchFragment.FOURTH_EDIT_TEXT -> valueOfTextViewFour =
                        valueOfTextViewFour.dropLast(1)
                }
            } else {
                when (numberOfTextView) {
                    StopwatchFragment.FIRST_EDIT_TEXT -> valueOfTextViewOne += char
                    StopwatchFragment.SECOND_EDIT_TEXT -> valueOfTextViewTwo += char
                    StopwatchFragment.THIRD_EDIT_TEXT -> valueOfTextViewThree += char
                    StopwatchFragment.FOURTH_EDIT_TEXT -> valueOfTextViewFour += char
                }
            }
            renderAddAthleteNumberState()
        }
    }

    fun addAthleteResult(btnNumber: Int) {
        if (isStarted) {
            var addAthleteNumber = ""
            when (btnNumber) {
                1 -> {
                    addAthleteNumber = valueOfTextViewOne
                    valueOfTextViewOne = ""
                }

                2 -> {
                    addAthleteNumber = valueOfTextViewTwo
                    valueOfTextViewTwo = ""
                }

                3 -> {
                    addAthleteNumber = valueOfTextViewThree
                    valueOfTextViewThree = ""
                }

                else -> {
                    addAthleteNumber = valueOfTextViewFour
                    valueOfTextViewFour = ""
                }
            }
            renderAddAthleteNumberState()

            viewModelScope.launch {
                if (currentRace.athletes.contains(addAthleteNumber)) {
                    for (athlete in athletesListInCurrentRace) {
                        if (athlete.number == addAthleteNumber) {
                            val newLapsTimeList = athlete.lapsTime.toMutableList()
                            newLapsTimeList.add(System.currentTimeMillis())
                            val athleteAfterUpdate = athlete.copy(
                                lapsTime = newLapsTimeList,
                                addLastResult = System.currentTimeMillis()
                            )
                            interactor.addAthleteResult(athleteAfterUpdate)
                            break
                        }
                    }
                } else {
                    if (addAthleteNumber.isNotBlank()) {
                        val id = getAthleteId(addAthleteNumber)
                        val newAthlete = Athlete(
                            id = id,
                            race = currentRace.startTime,
                            number = addAthleteNumber,
                            lapsTime = listOf<Long>(System.currentTimeMillis()),
                            addLastResult = System.currentTimeMillis(),
                            isExpandable = false
                        )
                        val newAthleteList = currentRace.athletes.toMutableList()
                        newAthleteList.add(newAthlete.number)
                        val updateRace = currentRace.copy(athletes = newAthleteList)
                        interactor.updateRace(updateRace)
                        interactor.addAthleteResult(newAthlete)
                    }
                    updateRaceInformation()
                }
            }
        }
    }

    private fun getAthleteId(number: String): Long {
        return (currentRace.startTime.toString() + number).toLong()
    }

    private fun renderTimer(state: TimerState) {
        _timerState.postValue(state)
    }

    private fun renderAthletesFastResult(athletesList: List<Athlete>) {
        _fastResultState.value = FastResultState.Content(athletesList, currentRace)
    }

    private fun renderAddAthleteNumberState() {
        if (isStarted) {
            _addAthleteNumberState.postValue(
                AddAthleteNumberState.IsStarted(
                    numberOfTextView,
                    valueOfTextViewOne,
                    valueOfTextViewTwo,
                    valueOfTextViewThree,
                    valueOfTextViewFour
                )
            )
        } else {
            numberOfTextView = 1
            valueOfTextViewOne = ""
            valueOfTextViewTwo = ""
            valueOfTextViewThree = ""
            valueOfTextViewFour = ""
            _addAthleteNumberState.postValue(AddAthleteNumberState.Default)
        }
    }

    companion object {
        private const val UPDATE_DELAY = 100L
    }
}