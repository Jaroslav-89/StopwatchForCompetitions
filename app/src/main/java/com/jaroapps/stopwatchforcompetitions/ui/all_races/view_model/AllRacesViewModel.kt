package com.jaroapps.stopwatchforcompetitions.ui.all_races.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.ui.all_races.view_model.state.AllRaceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllRacesViewModel @Inject constructor(
    private val interactor: StopwatchInteractor,
) : ViewModel() {

    private val _allRacesState = MutableLiveData<AllRaceState>()
    val allRacesState: LiveData<AllRaceState>
        get() = _allRacesState

    fun loadRaceHistory() {
        viewModelScope.launch {
            interactor.getAllRaces().collect() { raceList ->
                if (raceList.isEmpty()) {
                    _allRacesState.postValue(AllRaceState.Empty)
                } else {
                    _allRacesState.postValue(AllRaceState.Content(raceList = raceList.sortedByDescending { it.isFavorite }))
                }
            }
        }
    }

    fun toggleFavoriteBtn(race: Race) {
        viewModelScope.launch {
            if (race.isFavorite) {
                interactor.updateRace(race.copy(isFavorite = false))
            } else {
                interactor.updateRace(race.copy(isFavorite = true))
            }
        }
    }
}