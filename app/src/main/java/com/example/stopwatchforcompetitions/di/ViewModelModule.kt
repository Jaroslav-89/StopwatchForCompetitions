package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.StopwatchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        StopwatchViewModel()
    }

}
