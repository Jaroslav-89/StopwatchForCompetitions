package com.example.stopwatchforcompetitions.ui.stopwatch.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatchforcompetitions.databinding.FastResultItemBinding
import com.example.stopwatchforcompetitions.domain.model.Athlete
import com.example.stopwatchforcompetitions.util.Util.getTimeFormat

class FastResultAdapter :
    RecyclerView.Adapter<FastResultViewHolder>() {

    private var athletes = emptyList<Athlete>()

    fun updateFastResultAdapter(content: List<Athlete>) {
        athletes = content
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
        holder.bind(athletes[position])
    }

    override fun getItemCount() = athletes.size
}

class FastResultViewHolder(private val binding: FastResultItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(athlete: Athlete) {
        with(binding) {
            number.text = athlete.number
            totalLap.text = athlete.lapsTime.size.toString()
            totalTime.text = getTimeFormat(athlete.addLastResult - athlete.race)
        }
    }
}