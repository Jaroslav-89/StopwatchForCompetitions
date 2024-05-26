package com.jaroapps.stopwatchforcompetitions.di

import android.content.Context
import com.jaroapps.stopwatchforcompetitions.data.db.AppDataBase
import com.jaroapps.stopwatchforcompetitions.data.local_storage.SaveResultXls
import com.jaroapps.stopwatchforcompetitions.data.navigation.ExternalNavigator
import com.jaroapps.stopwatchforcompetitions.data.repository.SettingsRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.data.repository.SharingRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.data.repository.StopwatchRepositoryImpl
import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModuleHilt {

    @Provides
    @Singleton
    fun provideStopwatchRepository(
        database: AppDataBase,
        saveResultXls: SaveResultXls,
    ): StopwatchRepository {
        return StopwatchRepositoryImpl(
            database,
            saveResultXls,
        )
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSharingRepository(
        externalNavigator: ExternalNavigator,
        @ApplicationContext context: Context
    ): SharingRepository {
        return SharingRepositoryImpl(externalNavigator, context)
    }
}
