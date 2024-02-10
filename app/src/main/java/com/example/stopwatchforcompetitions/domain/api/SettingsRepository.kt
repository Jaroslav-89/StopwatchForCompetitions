package com.example.stopwatchforcompetitions.domain.api

import com.example.stopwatchforcompetitions.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(settings: ThemeSettings)
}