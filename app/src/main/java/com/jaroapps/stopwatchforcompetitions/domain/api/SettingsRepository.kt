package com.jaroapps.stopwatchforcompetitions.domain.api

import com.jaroapps.stopwatchforcompetitions.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(settings: ThemeSettings)
}