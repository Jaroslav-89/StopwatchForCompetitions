package com.jaroapps.stopwatchforcompetitions.domain.api

import com.jaroapps.stopwatchforcompetitions.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(settings: ThemeSettings)
}