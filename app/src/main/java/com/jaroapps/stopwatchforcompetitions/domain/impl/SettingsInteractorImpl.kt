package com.jaroapps.stopwatchforcompetitions.domain.impl

import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsRepository
import com.jaroapps.stopwatchforcompetitions.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        settingsRepository.updateThemeSettings(settings)
    }

}