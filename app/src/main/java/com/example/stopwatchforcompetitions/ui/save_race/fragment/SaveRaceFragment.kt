package com.example.stopwatchforcompetitions.ui.save_race.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.stopwatchforcompetitions.databinding.FragmentSaveRaceBinding
import com.example.stopwatchforcompetitions.ui.save_race.view_model.SaveRaceViewModel
import com.example.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import com.example.stopwatchforcompetitions.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaveRaceFragment : Fragment() {
    private lateinit var binding: FragmentSaveRaceBinding
    private val args: SaveRaceFragmentArgs by navArgs()
    private var startRaceData = 0L
    private val viewModel: SaveRaceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSaveRaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRaceInfo(args.startRaceDataSaveRace)

        binding.editBtn.setOnClickListener {
            val argument = startRaceData
            val action =
                SaveRaceFragmentDirections.actionSaveRaceFragmentToEditRaceFragment(argument)
            findNavController().navigate(action)
        }

        viewModel.saveRacesState.observe(viewLifecycleOwner) {
            renderRaceInformation(it)
        }
    }

    private fun renderRaceInformation(raceInfo: SaveRaceState) {
        if (raceInfo is SaveRaceState.Content) {
            if (raceInfo.race != null) {
                startRaceData = raceInfo.race.startTime
                with(binding) {
                    startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                    raceName.text = raceInfo.race.name
                    numberOfAthletes.text = raceInfo.race.athletes.size.toString()
                    lapDistance.text = raceInfo.race.lapDistance.toString()
                    athletes.text = raceInfo.athleteList.toString()
                }
            }
        }
    }
}