package com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.DetailResultItemBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.ui.common.ui.adapter.AthleteDiffCallback
import com.jaroapps.stopwatchforcompetitions.util.Util

class RaceDetailAdapter(private val clickListener: AthleteClickListener) :
    RecyclerView.Adapter<RaceDetailViewHolder>() {

    private var athletes = emptyList<Athlete>()
    private var race: Race? = null
    fun updateRaceDetailAdapter(athletesList: List<Athlete>, newRace: Race) {
        val diffResult = DiffUtil.calculateDiff(AthleteDiffCallback(athletes, athletesList))
        athletes = athletesList
        if (race != newRace) {
            notifyDataSetChanged()
        }
        race = newRace
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceDetailViewHolder {
        val binding = DetailResultItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RaceDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RaceDetailViewHolder, position: Int) {
        holder.lapsRv.setHasFixedSize(true)
        val adapter = LapsAdapter(athletes[position], race!!)
        holder.lapsRv.adapter = adapter
        holder.bind(athletes[position], race!!, clickListener)
    }

    override fun getItemCount() = athletes.size

    interface AthleteClickListener {
        fun onAthleteClick(athlete: Athlete)
        fun onAthleteNumberClick(athlete: Athlete)
    }
}

class RaceDetailViewHolder(private val binding: DetailResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val lapsRv = binding.lapsDetailRv
    fun bind(
        athlete: Athlete,
        race: Race,
        clickListener: RaceDetailAdapter.AthleteClickListener,
    ) {

        with(binding) {
            detailResultItemBg.setOnClickListener {
                clickListener.onAthleteClick(athlete)
            }
            editAthleteNumber.setOnClickListener {
                clickListener.onAthleteNumberClick(athlete)
            }
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
                openCloseLapsIc.setImageDrawable(
                    getDrawable(
                        itemView.context,
                        R.drawable.ic_close_laps_info
                    )
                )
            } else {
                lapsDetailRv.visibility = View.GONE
                openCloseLapsIc.setImageDrawable(
                    getDrawable(
                        itemView.context,
                        R.drawable.ic_open_laps_info
                    )
                )
            }
        }
    }
}