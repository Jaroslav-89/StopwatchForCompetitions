package com.jaroapps.stopwatchforcompetitions.data.navigation

import com.jaroapps.stopwatchforcompetitions.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(url: String)
    fun donateStreamLink(url: String)
    fun rateLink(url: String)
    fun openLink(url: String)
    fun openEmail(emailData: EmailData)
}