package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FastResultItemBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.FastResultAthleteHistory
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.util.Util
import com.jaroapps.stopwatchforcompetitions.util.Util.getTimeFormat

class FastResultAdapter :
    RecyclerView.Adapter<FastResultViewHolder>() {

    private var fastResultList = emptyList<FastResultAthleteHistory>()
    private var race: Race? = null

    fun updateFastResultAdapter(newFastResultList: List<FastResultAthleteHistory>, newRace: Race?) {
        val diffResult =
            DiffUtil.calculateDiff(FastResultDiffCallback(fastResultList, newFastResultList))
        fastResultList = newFastResultList
        race = newRace
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FastResultViewHolder {
        val binding = FastResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FastResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FastResultViewHolder, position: Int) {
        holder.bind(fastResultList[position], race!!)
    }

    override fun getItemCount() = fastResultList.size
}

class FastResultViewHolder(private val binding: FastResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(athlete: FastResultAthleteHistory, race: Race) {

        with(binding) {
            if (race.totalLapsInRace > 0) {
                if (athlete.currentLapNumber >= race.totalLapsInRace) {
                    fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke_race_done)
                } else {
                    fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke)
                }
            } else {
                fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke)
            }

            number.text = athlete.number
            totalLap.text = athlete.currentLapNumber.toString()
            lastLapTime.text = Util.getTimeFormat(athlete.currentLapTime)
            totalTime.text = getTimeFormat(athlete.addLastResult - athlete.race)
        }
    }
}