package com.example.stopwatchforcompetitions.ui.race_detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stopwatchforcompetitions.databinding.FragmentRaceDetailBinding
import com.example.stopwatchforcompetitions.ui.race_detail.fragment.adapter.RaceDetailAdapter
import com.example.stopwatchforcompetitions.ui.race_detail.view_model.RaceDetailViewModel
import com.example.stopwatchforcompetitions.ui.race_detail.view_model.state.RaceDetailState
import com.example.stopwatchforcompetitions.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel

class RaceDetailFragment : Fragment() {
    private lateinit var binding: FragmentRaceDetailBinding
    private val raceDetailAdapter = RaceDetailAdapter {
        viewModel.toggleLapDetail(it)
    }
    private var startRaceData = 0L
    private val viewModel: RaceDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRaceDetailAdapter()
        viewModel.getRaceInfo()

        binding.editBtn.setOnClickListener {
            val argument = startRaceData
            val action =
                RaceDetailFragmentDirections.actionRaceDetailFragmentToEditRaceFragment(argument)
            findNavController().navigate(action)
        }

        viewModel.raceDetailState.observe(viewLifecycleOwner) {
            renderRaceDetail(it)
        }
    }

    private fun setRaceDetailAdapter() {
        binding.raceDetailRv.adapter = raceDetailAdapter
    }

    private fun renderRaceDetail(raceInfo: RaceDetailState) {
        if (raceInfo is RaceDetailState.Content) {
            startRaceData = raceInfo.race.startTime
            with(binding) {
                startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                raceName.text = raceInfo.race.name
                lapDistance.text = raceInfo.race.lapDistance.toString()
                raceDetailAdapter.updateRaceDetailAdapter(raceInfo.athleteList, raceInfo.race)
            }
        }
    }
}