package com.jaroapps.stopwatchforcompetitions.di

import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.impl.SettingsInteractorImpl
import com.jaroapps.stopwatchforcompetitions.domain.impl.SharingInteractorImpl
import com.jaroapps.stopwatchforcompetitions.domain.impl.StopwatchInteractorImpl
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