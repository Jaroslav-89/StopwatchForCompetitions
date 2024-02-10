package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.domain.api.SettingsInteractor
import com.example.stopwatchforcompetitions.domain.api.SharingInteractor
import com.example.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.example.stopwatchforcompetitions.domain.impl.SettingsInteractorImpl
import com.example.stopwatchforcompetitions.domain.impl.SharingInteractorImpl
import com.example.stopwatchforcompetitions.domain.impl.StopwatchInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<StopwatchInteractor> {
        StopwatchInteractorImpl(
            repository = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(sharingRepository = get())
    }
}