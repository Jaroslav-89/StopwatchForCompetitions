package com.example.stopwatchforcompetitions.di

import com.example.stopwatchforcompetitions.data.repository.StopwatchRepositoryImpl
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
}