package com.jaroapps.stopwatchforcompetitions.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jaroapps.stopwatchforcompetitions.data.db.AppDataBase
import com.jaroapps.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.jaroapps.stopwatchforcompetitions.data.local_storage.impl.SaveResultXlsImpl
import com.jaroapps.stopwatchforcompetitions.data.navigation.ExternalNavigator
import com.jaroapps.stopwatchforcompetitions.data.navigation.impl.ExternalNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModuleHilt {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase {
        return Room.databaseBuilder(context, AppDataBase::class.java, "database.db")
            .addMigrations(
                AppDataBase.MIGRATION_2_3,
                AppDataBase.MIGRATION_3_4,
                AppDataBase.MIGRATION_4_5,
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideExternalNavigator(@ApplicationContext context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    @Provides
    @Singleton
    fun provideSaveResultXls(@ApplicationContext context: Context): SaveResultXls {
        return SaveResultXlsImpl(context)
    }
}
