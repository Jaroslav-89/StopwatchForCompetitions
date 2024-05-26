package com.jaroapps.stopwatchforcompetitions.data.repository

import android.content.Context
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.data.navigation.ExternalNavigator
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingRepository
import com.jaroapps.stopwatchforcompetitions.domain.model.EmailData

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingRepository {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun contactSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return context.getString(R.string.share_url)
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.user_agreement_url)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            emailTo = context.getString(R.string.email_support),
            emailSubject = context.getString(R.string.email_support_subject),
            emailText = context.getString(R.string.email_support_message)
        )
    }
}