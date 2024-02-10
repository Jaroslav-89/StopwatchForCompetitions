package com.example.stopwatchforcompetitions.data.repository

import android.content.Context
import com.example.stopwatchforcompetitions.app.App
import com.example.stopwatchforcompetitions.domain.api.SettingsRepository
import com.example.stopwatchforcompetitions.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(
            darkMode = (context as App).darkTheme
        )
    }

    override fun updateThemeSettings(settings: ThemeSettings) {
        (context as App).switchTheme(settings.darkMode)
    }
}