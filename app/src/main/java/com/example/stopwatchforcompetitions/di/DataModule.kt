package com.example.stopwatchforcompetitions.di

import androidx.room.Room
import com.example.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.example.stopwatchforcompetitions.data.converters.RaceDbConvertor
import com.example.stopwatchforcompetitions.data.db.AppDataBase
import com.example.stopwatchforcompetitions.data.local_storage.SaveResultXls
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory {
        AthleteDbConvertor()
    }

    factory {
        RaceDbConvertor()
    }

    single {
        SaveResultXls(
            context = androidContext(),
            athleteDbConvertor = get()
        )
    }
}