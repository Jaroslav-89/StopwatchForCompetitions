package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.impl.StopwatchInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<StopwatchInteractor> {
        StopwatchInteractorImpl(
            repository = get()
        )
    }
}