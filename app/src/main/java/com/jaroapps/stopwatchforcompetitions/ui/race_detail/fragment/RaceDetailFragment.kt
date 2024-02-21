package com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentRaceDetailBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment.adapter.RaceDetailAdapter
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.RaceDetailViewModel
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.RaceDetailState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.SortingScreenState
import com.jaroapps.stopwatchforcompetitions.util.Util
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
        setClickListeners()

        viewModel.raceDetailState.observe(viewLifecycleOwner) {
            renderRaceDetail(it)
        }

        viewModel.sortingScreenState.observe(viewLifecycleOwner) {
            renderSortingScreen(it)
        }
    }

    private fun setClickListeners() {
        with(binding) {
            editBtn.setOnClickListener {
                val argument = startRaceData
                val action =
                    RaceDetailFragmentDirections.actionRaceDetailFragmentToEditRaceFragment(argument)
                findNavController().navigate(action)
            }

            back.setOnClickListener {
                findNavController().navigateUp()
            }

            sortingBtn.setOnClickListener {
                viewModel.toggleSortingBtn()
            }

            sortPosFtL.setOnClickListener {
                viewModel.changeSorting(SortingState.POSITION_FIRST_TO_LAST_ORDER)
            }

            sortPosLtF.setOnClickListener {
                viewModel.changeSorting(SortingState.POSITION_LAST_TO_FIRST_ORDER)
            }

            sortNumFtL.setOnClickListener {
                viewModel.changeSorting(SortingState.NUMBER_FIRST_TO_LAST_ORDER)
            }

            sortNumLtF.setOnClickListener {
                viewModel.changeSorting(SortingState.NUMBER_LAST_TO_FIRST_ORDER)
            }

            overlay.setOnClickListener {
                viewModel.toggleSortingBtn()
            }
        }
    }

    private fun hideSortingMenu() {
        binding.sortingGroup.visibility = View.GONE
        binding.overlay.visibility = View.GONE
    }

    private fun setRaceDetailAdapter() {
        binding.raceDetailRv.adapter = raceDetailAdapter
    }

    private fun renderRaceDetail(raceInfo: RaceDetailState) {
        if (raceInfo is RaceDetailState.Content) {
            startRaceData = raceInfo.race.startTime
            with(binding) {
                startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                startTime.text = Util.convertLongToTime(raceInfo.race.startTime)
                raceName.text = raceInfo.race.name
                lapDistance.text = raceInfo.race.lapDistance.toString()
                totalLapInRace.text = raceInfo.race.totalLapsInRace.toString()
                raceDetailAdapter.updateRaceDetailAdapter(raceInfo.athleteList, raceInfo.race)
            }
        }
    }

    private fun renderSortingScreen(sortingScreenState: SortingScreenState) {
        when (sortingScreenState) {
            is SortingScreenState.Gone -> {
                binding.overlay.visibility = View.GONE
                binding.sortingGroup.visibility = View.GONE
                binding.sortingHeadingTv.text = getSortingText(sortingScreenState.sortingState)
            }

            is SortingScreenState.Visible -> {
                binding.overlay.visibility = View.VISIBLE
                binding.sortingGroup.visibility = View.VISIBLE
                showSelectSortingPosition(sortingScreenState.sortingState)
            }
        }
    }

    private fun getSortingText(sortingState: SortingState): String {
        return when (sortingState) {
            SortingState.POSITION_FIRST_TO_LAST_ORDER -> {
                binding.sortPosFtLIc.visibility = View.VISIBLE
                requireContext().getString(R.string.pos_first_to_last)
            }

            SortingState.POSITION_LAST_TO_FIRST_ORDER -> {
                binding.sortPosLtFIc.visibility = View.VISIBLE
                requireContext().getString(R.string.pos_last_to_first)
            }

            SortingState.NUMBER_FIRST_TO_LAST_ORDER -> {
                binding.sortNumFtLIc.visibility = View.VISIBLE
                requireContext().getString(R.string.num_first_to_last)
            }

            SortingState.NUMBER_LAST_TO_FIRST_ORDER -> {
                binding.sortNumLtFIc.visibility = View.VISIBLE
                requireContext().getString(R.string.num_last_to_first)
            }
        }
    }

    private fun showSelectSortingPosition(sortingState: SortingState) {
        hideSortingIcon()
        when (sortingState) {
            SortingState.POSITION_FIRST_TO_LAST_ORDER -> {
                binding.sortPosFtLIc.visibility = View.VISIBLE
            }

            SortingState.POSITION_LAST_TO_FIRST_ORDER -> {
                binding.sortPosLtFIc.visibility = View.VISIBLE
            }

            SortingState.NUMBER_FIRST_TO_LAST_ORDER -> {
                binding.sortNumFtLIc.visibility = View.VISIBLE
            }

            SortingState.NUMBER_LAST_TO_FIRST_ORDER -> {
                binding.sortNumLtFIc.visibility = View.VISIBLE
            }
        }
    }

    private fun hideSortingIcon() {
        binding.sortPosFtLIc.visibility = View.GONE
        binding.sortPosLtFIc.visibility = View.GONE
        binding.sortNumFtLIc.visibility = View.GONE
        binding.sortNumLtFIc.visibility = View.GONE
    }
}