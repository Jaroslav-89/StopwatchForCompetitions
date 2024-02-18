package com.example.stopwatchforcompetitions.ui.support_project.view_model

import androidx.lifecycle.ViewModel
import com.example.stopwatchforcompetitions.domain.api.SharingInteractor

class SupportProjectViewModel(
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    fun supportProject() {
        sharingInteractor.donateStream()
    }

    fun rateProject() {
        sharingInteractor.rateApp()
    }
}