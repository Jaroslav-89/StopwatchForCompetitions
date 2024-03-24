package com.jaroapps.stopwatchforcompetitions.ui.save_race.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.SortingScreenState
import com.jaroapps.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveRaceViewModel @Inject constructor(
    private val interactor: StopwatchInteractor,
) : ViewModel() {

    private var athletes = emptyList<Athlete>()
    private var currentRace = Race()
    private var sortingState: SortingState = SortingState.POSITION_FIRST_TO_LAST_ORDER
    private var sortingScreenIsVisible = false

    private val _saveRacesState = MutableLiveData<SaveRaceState>(SaveRaceState.Empty)
    val saveRacesState: LiveData<SaveRaceState>
        get() = _saveRacesState

    private val _sortingScreenState =
        MutableLiveData<SortingScreenState>(
            SortingScreenState.Gone(SortingState.POSITION_FIRST_TO_LAST_ORDER)
        )
    val sortingScreenState: LiveData<SortingScreenState>
        get() = _sortingScreenState

    fun getRaceInfo(data: Long) {
        viewModelScope.launch {
            if (data != 0L) {
                val race = interactor.getRaceInformation(data)
                currentRace = race

                interactor.getAllAthletesInRace(data).collect() {
                    athletes = it
                    renderState(race = currentRace, athleteList = athletes)
                }
            } else {
                val race = interactor.getLastRace()
                currentRace = race

                interactor.getAllAthletesInRace(race.startTime).collect() {
                    athletes = it
                    renderState(race = currentRace, athleteList = athletes)
                }
            }
        }
    }

    fun toggleFavoriteBtn() {
        viewModelScope.launch {
            if (currentRace.isFavorite) {
                interactor.updateRace(currentRace.copy(isFavorite = false))
            } else {
                interactor.updateRace(currentRace.copy(isFavorite = true))
            }

            val race = interactor.getRaceInformation(currentRace.startTime)
            currentRace = race
            renderState(race = currentRace, athleteList = athletes)
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

    fun saveResultInXls(uri: Uri) {
        viewModelScope.launch {
            interactor.saveResultInXls(currentRace.startTime, uri)
        }
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
        _saveRacesState.postValue(SaveRaceState.Content(race, sortedList))
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