package com.jaroapps.stopwatchforcompetitions.ui.stopwatch.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FastResultItemBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.util.Util
import com.jaroapps.stopwatchforcompetitions.util.Util.getTimeFormat

class FastResultAdapter :
    RecyclerView.Adapter<FastResultViewHolder>() {

    private var athletes = emptyList<Athlete>()
    private var race: Race? = null

    fun updateFastResultAdapter(content: List<Athlete>, newRace: Race?) {
        athletes = content
        race = newRace
        this.notifyDataSetChanged()
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
        holder.bind(athletes[position], race!!)
    }

    override fun getItemCount() = athletes.size
}

class FastResultViewHolder(private val binding: FastResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(athlete: Athlete, race: Race) {

        val currentLapTime = if (athlete.lapsTime.size > 2) {
            athlete.lapsTime[athlete.lapsTime.size - 1] - athlete.lapsTime[athlete.lapsTime.size - 2]
        } else {
            athlete.lapsTime[athlete.lapsTime.size - 1] - athlete.race
        }
        with(binding) {
            if (race.totalLapsInRace > 0) {
                if (athlete.lapsTime.size >= race.totalLapsInRace) {
                    fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke_race_done)
                } else {
                    fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke)
                }
            } else {
                fastResultItemBg.setBackgroundResource(R.drawable.bg_fast_result_stroke)
            }

            number.text = athlete.number
            totalLap.text = athlete.lapsTime.size.toString()
            lastLapTime.text = Util.getTimeFormat(currentLapTime)
            totalTime.text = getTimeFormat(athlete.addLastResult - athlete.race)
        }
    }
}