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

    private val _saveRacesState = MutableLiveData<SaveRaceState>(SaveRaceState.Empty)
    val saveRacesState: LiveData<SaveRaceState>
        get() = _saveRacesState

    fun getRaceInfo(data: Long) {
        viewModelScope.launch {
            if (data != 0L) {
                val race = interactor.getRaceInformation(data)

                interactor.getAllAthletesInRace(data).collect() {
                    renderState(race = race, athleteList = it)
                }
            } else {
                val race = interactor.getLastRace()

                interactor.getAllAthletesInRace(race.startTime).collect() {
                    renderState(race = race, athleteList = it)
                }
            }
        }
    }

    private fun renderState(race: Race, athleteList: List<Athlete>) {
        _saveRacesState.postValue(SaveRaceState.Content(race, athleteList))
    }

}