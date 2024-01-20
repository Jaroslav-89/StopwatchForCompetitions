package com.example.stopwatchforcompetitions.ui.all_races.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatchforcompetitions.databinding.RaceItemBinding
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.util.Util.convertLongToDate

class AllRacesAdapter(private val clickListener: RaceClickListener) :
    RecyclerView.Adapter<AllRacesViewHolder>() {

    private var raceList = emptyList<Race>()

    fun updateFastResultAdapter(content: List<Race>) {
        raceList = content
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRacesViewHolder {
        val binding = RaceItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllRacesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllRacesViewHolder, position: Int) {
        holder.bind(raceList[position])
        holder.itemView.setOnClickListener { clickListener.onRaceClick(raceList[position]) }
    }

    override fun getItemCount() = raceList.size

    fun interface RaceClickListener {
        fun onRaceClick(race: Race)
    }
}

class AllRacesViewHolder(private val binding: RaceItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(race: Race) {
        with(binding) {
            startDate.text = convertLongToDate(race.startTime)
            raceName.text = race.name
            numberOfAthletes.text = race.athletes.size.toString()
            lapDistance.text = race.lapDistance.toString()
        }
    }
}