package com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state

import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState

sealed interface SortingScreenState {
    data class Gone(val sortingState: SortingState) : SortingScreenState
    data class Visible(val sortingState: SortingState) : SortingScreenState
}
