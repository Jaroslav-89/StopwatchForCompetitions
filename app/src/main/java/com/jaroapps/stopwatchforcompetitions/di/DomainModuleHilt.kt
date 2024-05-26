package com.jaroapps.stopwatchforcompetitions.di

import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingRepository
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.StopwatchRepository
import com.jaroapps.stopwatchforcompetitions.domain.impl.SettingsInteractorImpl
import com.jaroapps.stopwatchforcompetitions.domain.impl.SharingInteractorImpl
import com.jaroapps.stopwatchforcompetitions.domain.impl.StopwatchInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModuleHilt {

    @Provides
    @Singleton
    fun provideStopwatchInteractor(repository: StopwatchRepository): StopwatchInteractor {
        return StopwatchInteractorImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSettingsInteractor(settingsRepository: SettingsRepository): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository)
    }

    @Provides
    @Singleton
    fun provideSharingInteractor(sharingRepository: SharingRepository): SharingInteractor {
        return SharingInteractorImpl(sharingRepository)
    }
}
