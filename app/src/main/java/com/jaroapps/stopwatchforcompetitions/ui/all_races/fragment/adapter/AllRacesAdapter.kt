package com.jaroapps.stopwatchforcompetitions.ui.all_races.fragment.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.RaceItemBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.util.Util.convertLongToDate
import com.jaroapps.stopwatchforcompetitions.util.Util.convertLongToTime

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
            favorite.setImageDrawable(getFavoriteToggleDrawable(race.isFavorite))

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

    private fun getFavoriteToggleDrawable(isFavorite: Boolean?): Drawable? {
        return if (isFavorite == null || !isFavorite)
            getDrawable(itemView.context, R.drawable.ic_inactive_favorite)
        else
            getDrawable(itemView.context, R.drawable.ic_active_favorite)
    }
}