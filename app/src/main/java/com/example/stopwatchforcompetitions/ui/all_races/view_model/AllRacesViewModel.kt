package com.example.stopwatchforcompetitions.ui.all_races.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.ui.all_races.view_model.state.AllRaceState
import kotlinx.coroutines.launch

class AllRacesViewModel(private val interactor: StopwatchInteractor) : ViewModel() {

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
}