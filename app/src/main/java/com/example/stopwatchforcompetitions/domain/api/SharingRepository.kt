package com.example.stopwatchforcompetitions.domain.api

interface SharingRepository {
    fun shareApp()
    fun donateStream()
    fun rateApp()
    fun openTerms()
    fun contactSupport()
}