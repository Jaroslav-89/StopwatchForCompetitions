package com.jaroapps.stopwatchforcompetitions.domain.impl

import com.jaroapps.stopwatchforcompetitions.domain.api.SharingInteractor
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingRepository

class SharingInteractorImpl(private val sharingRepository: SharingRepository) : SharingInteractor {
    override fun shareApp() {
        sharingRepository.shareApp()
    }

    override fun contactSupport() {
        sharingRepository.contactSupport()
    }

    override fun openTerms() {
        sharingRepository.openTerms()
    }
}