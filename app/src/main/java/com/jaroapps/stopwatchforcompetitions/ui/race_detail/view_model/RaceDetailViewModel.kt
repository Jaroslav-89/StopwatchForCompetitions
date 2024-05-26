package com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.RaceDetailState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.SortingScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RaceDetailViewModel @Inject constructor(
    private val interactor: StopwatchInteractor,
) : ViewModel() {

    private var athletes = emptyList<Athlete>()

    private var currentRace = Race()
    private var newAthleteNumber = ""
    private var sortingState: SortingState = SortingState.POSITION_FIRST_TO_LAST_ORDER
    private var sortingScreenIsVisible = false
    private val _raceDetailState = MutableLiveData<RaceDetailState>(RaceDetailState.Empty)
    val raceDetailState: LiveData<RaceDetailState>
        get() = _raceDetailState

    private val _sortingScreenState =
        MutableLiveData<SortingScreenState>(
            SortingScreenState.Gone(SortingState.POSITION_FIRST_TO_LAST_ORDER)
        )
    val sortingScreenState: LiveData<SortingScreenState>
        get() = _sortingScreenState

    private val _numberAvailable = MutableLiveData<Boolean>()
    val numberAvailable: LiveData<Boolean>
        get() = _numberAvailable

    fun getRaceInfo() {
        viewModelScope.launch {
            val race = interactor.checkRaceIsStarted()
            if (race != null) {
                currentRace = race
                interactor.getAllAthletesInRace(currentRace.startTime).collect() {
                    athletes = it
                    renderState(race = currentRace, athleteList = athletes)
                }
            }
        }
    }

    fun checkNumberAvailability(number: String) {
        if (number.trim().isNotBlank()) {
            newAthleteNumber = number
            if (currentRace.athletes.contains(number)) {
                _numberAvailable.postValue(false)
            } else {
                _numberAvailable.postValue(true)
            }
        }
    }

    fun changeAthleteNumber(athleteForChange: Athlete) {
        val newAthlete = athleteForChange.copy(number = newAthleteNumber)
        val newAthleteList = currentRace.athletes.toMutableList()
        newAthleteList.remove(athleteForChange.number)
        newAthleteList.add(newAthlete.number)
        val newRace = currentRace.copy(athletes = newAthleteList)
        currentRace = newRace
        viewModelScope.launch {
            interactor.changeAthleteNumber(athleteForChange, newAthlete, currentRace)
            val race = interactor.checkRaceIsStarted()
            if (race != null) {
                currentRace = race
            }
        }
    }

    fun toggleLapDetail(updateAthlete: Athlete) {
        val athlete = athletes.filter { it.number == updateAthlete.number }
        val newList = athletes.toMutableList()
        newList.remove(athlete[0])
        if (updateAthlete.isExpandable) {
            newList.add(athlete[0].copy(isExpandable = false))
        } else {
            newList.add(athlete[0].copy(isExpandable = true))
        }
        athletes = newList
        renderState(currentRace, newList)
    }

    fun toggleSortingBtn() {
        if (sortingScreenIsVisible) {
            sortingScreenIsVisible = false
            _sortingScreenState.value = SortingScreenState.Gone(sortingState)
        } else {
            sortingScreenIsVisible = true
            _sortingScreenState.value = SortingScreenState.Visible(sortingState)
        }
    }

    fun changeSorting(sortType: SortingState) {
        sortingState = sortType
        toggleSortingBtn()
        renderState(currentRace, athletes)
    }

    private fun renderState(race: Race, athleteList: List<Athlete>) {
        val getAthleteListWithPositions = getAthleteListWithPositions(athleteList)
        val sortedList = when (sortingState) {
            SortingState.POSITION_FIRST_TO_LAST_ORDER -> {
                getAthleteListWithPositions.sortedBy { it.position }
            }

            SortingState.POSITION_LAST_TO_FIRST_ORDER -> {
                getAthleteListWithPositions.sortedByDescending { it.position }
            }

            SortingState.NUMBER_FIRST_TO_LAST_ORDER -> {

                getAthleteListWithPositions.sortedBy { it.number.toInt() }
            }

            SortingState.NUMBER_LAST_TO_FIRST_ORDER -> {
                getAthleteListWithPositions.sortedByDescending { it.number.toInt() }
            }
        }
        _raceDetailState.postValue(RaceDetailState.Content(race, sortedList))
    }

    private fun getAthleteListWithPositions(athleteList: List<Athlete>): List<Athlete> {
        val athleteListSortedByPosition =
            athleteList.sortedBy { it.addLastResult }.sortedByDescending { it.lapsTime.size }
        val athleteListWithPositions = mutableListOf<Athlete>()
        for (athlete in athleteListSortedByPosition) {
            athleteListWithPositions.add(
                athlete.copy(
                    position = athleteListSortedByPosition.indexOf(
                        athlete
                    ) + 1
                )
            )
        }
        return athleteListWithPositions
    }
}