package com.jaroapps.stopwatchforcompetitions.ui.common.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete

class AthleteDiffCallback(
    private val oldList: List<Athlete>,
    private val newList: List<Athlete>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].number == newList[newItemPosition].number
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
