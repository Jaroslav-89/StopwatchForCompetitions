package com.jaroapps.stopwatchforcompetitions.di

import androidx.room.Room
import com.jaroapps.stopwatchforcompetitions.data.converters.AthleteDbConvertor
import com.jaroapps.stopwatchforcompetitions.data.converters.RaceDbConvertor
import com.jaroapps.stopwatchforcompetitions.data.db.AppDataBase
import com.jaroapps.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.jaroapps.stopwatchforcompetitions.data.navigation.ExternalNavigator
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