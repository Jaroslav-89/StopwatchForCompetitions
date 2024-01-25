package com.example.stopwatchforcompetitions.ui.save_race.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import kotlinx.coroutines.launch

class SaveRaceViewModel(
    private val interactor: StopwatchInteractor
) : ViewModel() {

    private var athletes = emptyList<Athlete>()

    private var currentRace = Race(
        startTime = 0,
        name = "",
        description = "",
        imgUrl = "",
        lapDistance = 0,
        athletes = emptyList(),
        isStarted = false
    )

    private val _saveRacesState = MutableLiveData<SaveRaceState>(SaveRaceState.Empty)
    val saveRacesState: LiveData<SaveRaceState>
        get() = _saveRacesState

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

    fun toggleLapDetail(updateAthlete: Athlete) {
        for (athlete in athletes) {
            if (athlete.number == updateAthlete.number) {
                val newList = athletes.toMutableList()
                newList.remove(athlete)
                if (updateAthlete.isExpandable) {
                    newList.add(athlete.copy(isExpandable = false))
                } else {
                    newList.add(athlete.copy(isExpandable = true))
                }
                athletes = newList
                renderState(currentRace, newList)
                break
            }
        }
    }

    private fun renderState(race: Race, athleteList: List<Athlete>) {
        _saveRacesState.postValue(SaveRaceState.Content(race, athleteList))
    }

}