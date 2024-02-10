package com.example.stopwatchforcompetitions.domain.impl

import com.example.stopwatchforcompetitions.domain.api.SharingInteractor
import com.example.stopwatchforcompetitions.domain.api.SharingRepository

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