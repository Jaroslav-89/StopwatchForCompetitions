package com.example.stopwatchforcompetitions.ui.save_race.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.DetailResultItemBinding
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.util.Util

class SaveRaceAdapter(private val clickListener: AthleteClickListener) :
    RecyclerView.Adapter<SaveRaceViewHolder>() {

    private var athletes = emptyList<Athlete>()
    private var race: Race? = null
    fun updateRaceDetailAdapter(athletesList: List<Athlete>, newRace: Race) {
        athletes = athletesList
        race = newRace
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaveRaceViewHolder {
        val binding = DetailResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SaveRaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SaveRaceViewHolder, position: Int) {
        holder.lapsRv.setHasFixedSize(true)
        val adapter = SaveRaceLapsAdapter(athletes[position], race!!)
        holder.lapsRv.adapter = adapter
        holder.bind(athletes[position], position, race!!)
        holder.itemView.setOnClickListener {
            clickListener.onAthleteClick(athletes[position])
        }
    }

    override fun getItemCount() = athletes.size

    fun interface AthleteClickListener {
        fun onAthleteClick(athlete: Athlete)
    }
}

class SaveRaceViewHolder(private val binding: DetailResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val lapsRv = binding.lapsDetailRv

    fun bind(athlete: Athlete, position: Int, race: Race) {

        with(binding) {
            if (race.totalLapsInRace > 0) {
                if (athlete.lapsTime.size >= race.totalLapsInRace) {
                    detailResultItemBg.setBackgroundResource(R.drawable.bg_athlete_result_laps_done_item)
                } else {
                    detailResultItemBg.setBackgroundResource(R.drawable.bg_athlete_result_in_progress_item)
                }
            } else {
                detailResultItemBg.setBackgroundResource(R.drawable.bg_athlete_result_in_progress_item)
            }

            athletePosition.text = athlete.position.toString()
            athleteNumber.text = athlete.number
            totalLaps.text = athlete.lapsTime.size.toString()
            val totalDistance = race.lapDistance * athlete.lapsTime.size
            val totalRaceTime = athlete.addLastResult - athlete.race
            averagePace.text = Util.convertToPace(totalDistance, totalRaceTime)
            averageSpeed.text = Util.convertToSpeed(totalDistance, totalRaceTime)
            totalTime.text = Util.getTimeFormat(athlete.addLastResult - athlete.race)
            if (athlete.isExpandable) {
                lapsDetailRv.visibility = View.VISIBLE
                openCloseLapsIc.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_close_laps_info))
            } else {
                lapsDetailRv.visibility = View.GONE
                openCloseLapsIc.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_open_laps_info))
            }
        }
    }
}