package com.example.stopwatchforcompetitions.ui.edit_race.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.model.Race
import kotlinx.coroutines.launch

class EditRaceViewModel(
    private val interactor: StopwatchInteractor
) : ViewModel() {

    private var raceBeforeChange: Race? = null
    private var raceAfterChange: Race? = null

    private val _editRacesState = MutableLiveData<Race>()
    val editRacesState: LiveData<Race>
        get() = _editRacesState

    fun getRaceByData(data: Long) {
        viewModelScope.launch {
            if (raceBeforeChange == null) {
                raceBeforeChange = interactor.getRaceInformation(data)
            }
            if (raceAfterChange == null) {
                raceAfterChange = raceBeforeChange
            }
            renderState(raceAfterChange!!)
        }
    }

    fun changeRace() {
        viewModelScope.launch {
            interactor.updateRace(raceAfterChange!!)
            raceBeforeChange = null
            raceAfterChange = null
        }
    }

    fun deleteRace() {
        viewModelScope.launch {
            interactor.deleteDaceAndAthletes(raceBeforeChange?.startTime ?: 0)
            raceBeforeChange = null
            raceAfterChange = null
        }
    }

    fun editNameText(text: String) {
        raceAfterChange = raceAfterChange?.copy(name = text)
        renderState(raceAfterChange!!)
    }

    fun editLapDistance(text: String) {
        raceAfterChange = raceAfterChange?.copy(lapDistance = text.trim().toInt())
        renderState(raceAfterChange!!)
    }

    fun editDescriptionText(text: String) {
        raceAfterChange = raceAfterChange?.copy(description = text)
        renderState(raceAfterChange!!)
    }

    fun deleteRaceImg() {
        raceAfterChange = raceAfterChange?.copy(imgUrl = "")
        renderState(raceAfterChange!!)
    }

    fun editPlaceHolderImg(img: String) {
        raceAfterChange = raceAfterChange?.copy(imgUrl = img)
        renderState(raceAfterChange!!)
    }

    private fun renderState(race: Race) {
        _editRacesState.postValue(race)
    }
}