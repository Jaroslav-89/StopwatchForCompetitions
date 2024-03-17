package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.fragment.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory

class FastResultDiffCallback(
    private val oldList: List<FastResultAthleteHistory>,
    private val newList: List<FastResultAthleteHistory>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].addLastResult == newList[newItemPosition].addLastResult
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
