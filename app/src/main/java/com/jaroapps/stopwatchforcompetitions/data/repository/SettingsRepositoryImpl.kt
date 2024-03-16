package com.jaroapps.stopwatchforcompetitions.data.repository

import android.content.Context
import com.jaroapps.stopwatchforcompetitions.app.App
import com.jaroapps.stopwatchforcompetitions.domain.api.SettingsRepository
import com.jaroapps.stopwatchforcompetitions.domain.model.ThemeSettings

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