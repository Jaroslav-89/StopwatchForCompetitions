package com.example.stopwatchforcompetitions.ui.race_detail.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.ui.race_detail.view_model.state.RaceDetailState
import kotlinx.coroutines.launch

class RaceDetailViewModel(
    private val interactor: StopwatchInteractor
) : ViewModel() {

    private var athletes = emptyList<Athlete>()

    private var currentRace = Race()

    private val _raceDetailState = MutableLiveData<RaceDetailState>(RaceDetailState.Empty)
    val raceDetailState: LiveData<RaceDetailState>
        get() = _raceDetailState

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

    private fun renderState(race: Race, athleteList: List<Athlete>) {
        _raceDetailState.postValue(RaceDetailState.Content(race, athleteList))
    }
}