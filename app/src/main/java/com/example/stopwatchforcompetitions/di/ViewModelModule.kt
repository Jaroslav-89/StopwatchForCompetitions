package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.ui.all_races.view_model.AllRacesViewModel
import com.example.stopwatchforcompetitions.ui.edit_race.view_model.EditRaceViewModel
import com.example.stopwatchforcompetitions.ui.race_detail.view_model.RaceDetailViewModel
import com.example.stopwatchforcompetitions.ui.save_race.view_model.SaveRaceViewModel
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.StopwatchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        StopwatchViewModel(
            interactor = get()
        )
    }

    viewModel {
        AllRacesViewModel(
            interactor = get()
        )
    }

    viewModel {
        SaveRaceViewModel(
            interactor = get()
        )
    }

    viewModel {
        EditRaceViewModel(
            interactor = get()
        )
    }

    viewModel {
        RaceDetailViewModel(
            interactor = get()
        )
    }
}
