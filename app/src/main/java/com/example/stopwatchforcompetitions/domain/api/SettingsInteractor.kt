package com.example.stopwatchforcompetitions.domain.api

import com.example.stopwatchforcompetitions.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSettings(settings: ThemeSettings)
}