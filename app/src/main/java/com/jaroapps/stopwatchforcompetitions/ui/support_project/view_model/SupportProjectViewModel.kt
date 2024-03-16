package com.jaroapps.stopwatchforcompetitions.ui.support_project.view_model

import androidx.lifecycle.ViewModel
import com.jaroapps.stopwatchforcompetitions.domain.api.SharingInteractor

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