package com.example.stopwatchforcompetitions.data.repository

import android.content.Context
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.data.navigation.ExternalNavigator
import com.example.stopwatchforcompetitions.domain.api.SharingRepository
import com.example.stopwatchforcompetitions.domain.model.EmailData

class SharingRepositoryImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
) : SharingRepository {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun donateStream() {
        externalNavigator.donateStreamLink(getDonateStreamLink())
    }

    override fun rateApp() {
        externalNavigator.rateLink(getRateAppLink())
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

    private fun getDonateStreamLink(): String {
        return context.getString(R.string.donate_stream_url)
    }

    private fun getRateAppLink(): String {
        return context.getString(R.string.rate_app_url)
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