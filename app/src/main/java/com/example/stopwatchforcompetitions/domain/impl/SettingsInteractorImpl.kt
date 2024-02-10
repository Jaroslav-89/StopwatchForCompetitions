package com.example.stopwatchforcompetitions.domain.impl

import com.example.stopwatchforcompetitions.domain.api.SettingsInteractor
import com.example.stopwatchforcompetitions.domain.api.SettingsRepository
import com.example.stopwatchforcompetitions.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        settingsRepository.updateThemeSettings(settings)
    }

}