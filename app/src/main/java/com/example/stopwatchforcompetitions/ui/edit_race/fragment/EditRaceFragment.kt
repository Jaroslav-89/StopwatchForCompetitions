package com.example.stopwatchforcompetitions.ui.edit_race.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.stopwatchforcompetitions.databinding.FragmentEditRaceBinding
import com.example.stopwatchforcompetitions.ui.edit_race.view_model.EditRaceViewModel
import com.example.stopwatchforcompetitions.ui.edit_race.view_model.state.EditRaceState
import com.example.stopwatchforcompetitions.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditRaceFragment : Fragment() {
    private lateinit var binding: FragmentEditRaceBinding
    private val args: EditRaceFragmentArgs by navArgs()
    private val viewModel: EditRaceViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditRaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRaceInfo(args.startRaceDataEditRace)

        viewModel.editRacesState.observe(viewLifecycleOwner) {
            renderRaceInformation(it)
        }
    }

    private fun renderRaceInformation(raceInfo: EditRaceState) {
        if (raceInfo is EditRaceState.Content) {
            with(binding) {
                startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                raceName.text = raceInfo.race.name
                raceDescription.text = raceInfo.race.description
                lapDistance.text = raceInfo.race.lapDistance.toString()
                imgUrl.text = raceInfo.race.imgUrl
            }
        }
    }
}