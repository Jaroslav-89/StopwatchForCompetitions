package com.example.stopwatchforcompetitions.ui.all_races.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.RaceItemBinding
import com.example.stopwatchforcompetitions.domain.model.Race
import com.example.stopwatchforcompetitions.util.Util.convertLongToDate
import com.example.stopwatchforcompetitions.util.Util.convertLongToTime

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
            startTime.text = convertLongToTime(race.startTime)
            raceName.text = race.name
            numberOfAthletes.text = race.athletes.size.toString()
            lapDistance.text = race.lapDistance.toString()

            Glide.with(itemView)
                .load(race.imgUrl)
                .placeholder(R.drawable.img_competition_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        itemView.resources.getDimensionPixelSize(R.dimen.race_cover_corner_radius_all_race_fragment)
                    ),
                )
                .into(raceImg)
        }
    }
}