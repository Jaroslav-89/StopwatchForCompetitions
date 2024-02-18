package com.example.stopwatchforcompetitions.di

import androidx.room.Room
import com.example.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.example.stopwatchforcompetitions.data.converters.RaceDbConvertor
import com.example.stopwatchforcompetitions.data.db.AppDataBase
import com.example.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.example.stopwatchforcompetitions.data.navigation.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .addMigrations(
                AppDataBase.MIGRATION_2_3,
                AppDataBase.MIGRATION_3_4,
            )
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

    single {
        ExternalNavigator(context = androidContext())
    }
}