package com.jaroapps.stopwatchforcompetitions.di

import com.jaroapps.stopwatchforcompetitions.data.repository.SettingsRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.data.repository.SharingRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.data.repository.StopwatchRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<StopwatchRepository> {
        StopwatchRepositoryImpl(
            dataBase = get(),
            raceDbConvertor = get(),
            athleteDbConvertor = get(),
            saveResultXls = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(context = get())
    }

    single<SharingRepository> {
        SharingRepositoryImpl(externalNavigator = get(), context = get())
    }
}