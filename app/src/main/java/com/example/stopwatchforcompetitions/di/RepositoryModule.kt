package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.data.repository.SettingsRepositoryImpl
import com.example.stopwatchforcompetitions.data.repository.SharingRepositoryImpl
import com.example.stopwatchforcompetitions.data.repository.StopwatchRepositoryImpl
import com.example.stopwatchforcompetitions.domain.api.SettingsRepository
import com.example.stopwatchforcompetitions.domain.api.SharingRepository
import com.example.stopwatchforcompetitions.domain.api.StopwatchRepository
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