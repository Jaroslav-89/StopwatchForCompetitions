package com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.stopwatchforcompetitions.databinding.DetailLapItemBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.util.Util
import com.jaroapps.stopwatchforcompetitions.util.Util.convertToPace
import com.jaroapps.stopwatchforcompetitions.util.Util.convertToSpeed

class LapsAdapter(private val athlete: Athlete, private val race: Race) :
    RecyclerView.Adapter<LapsViewHolder>() {

    private var laps = athlete.lapsTime

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapsViewHolder {
        val binding = DetailLapItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LapsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LapsViewHolder, position: Int) {
        holder.bind(position, athlete, race.lapDistance)
    }

    override fun getItemCount() = laps.size
}

class LapsViewHolder(private val binding: DetailLapItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, athlete: Athlete, lapDistance: Int) {
        val numberOfLap = position + 1
        val currentLapTime = if (position > 0) {
            val previousLap = position - 1
            athlete.lapsTime[position] - athlete.lapsTime[previousLap]
        } else {
            athlete.lapsTime[position] - athlete.race
        }
        with(binding) {
            lapNumber.text = numberOfLap.toString()
            pace.text = convertToPace(lapDistance, currentLapTime)
            speed.text = convertToSpeed(lapDistance, currentLapTime)
            lapTime.text = Util.getTimeFormat(currentLapTime)
        }
    }
}