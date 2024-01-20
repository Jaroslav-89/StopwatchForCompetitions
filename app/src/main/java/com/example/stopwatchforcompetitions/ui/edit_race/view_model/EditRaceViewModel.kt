package com.example.stopwatchforcompetitions.ui.edit_race.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.ui.edit_race.view_model.state.EditRaceState
import kotlinx.coroutines.launch

class EditRaceViewModel(
    private val interactor: StopwatchInteractor
) : ViewModel() {

    private val _editRacesState = MutableLiveData<EditRaceState>(EditRaceState.Empty)
    val editRacesState: LiveData<EditRaceState>
        get() = _editRacesState

    fun getRaceInfo(data: Long) {
        viewModelScope.launch {
            val race = interactor.getRaceInformation(data)

            renderState(race)
        }
    }

    private fun renderState(race: Race) {
        _editRacesState.postValue(EditRaceState.Content(race))
    }
}