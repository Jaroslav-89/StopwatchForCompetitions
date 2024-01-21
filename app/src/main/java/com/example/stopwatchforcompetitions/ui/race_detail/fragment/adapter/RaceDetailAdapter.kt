package com.example.stopwatchforcompetitions.ui.race_detail.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.DetailResultItemBinding
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.util.Util

class RaceDetailAdapter(private val clickListener: AthleteClickListener) :
    RecyclerView.Adapter<RaceDetailViewHolder>() {

    private var athletes = emptyList<Athlete>()
    private var race: Race? = null
    fun updateRaceDetailAdapter(athletesList: List<Athlete>, newRace: Race) {
        athletes =
            athletesList.sortedBy { it.addLastResult }.sortedByDescending { it.lapsTime.size }
        race = newRace
        this.notifyDataSetChanged()
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
        holder.bind(athletes[position], position, race!!)
        holder.itemView.setOnClickListener { clickListener.onAthleteClick(athletes[position]) }
    }

    override fun getItemCount() = athletes.size

    fun interface AthleteClickListener {
        fun onAthleteClick(athlete: Athlete)
    }
}

class RaceDetailViewHolder(private val binding: DetailResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val lapsRv = binding.lapsDetailRv
    fun bind(athlete: Athlete, position: Int, race: Race) {

        with(binding) {
            val athletePositionInRace = (position + 1)
            athletePosition.text = athletePositionInRace.toString()
            athleteNumber.text = athlete.number
            totalLaps.text = athlete.lapsTime.size.toString()
            totalTime.text = Util.getTimeFormat(athlete.addLastResult - athlete.race)
            if (athlete.isExpandable) {
                lapInfoGroup.visibility = View.VISIBLE
                openCloseLapsIc.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_close_laps_info))
            } else {
                lapInfoGroup.visibility = View.GONE
                openCloseLapsIc.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_open_laps_info))
            }
        }
    }
}