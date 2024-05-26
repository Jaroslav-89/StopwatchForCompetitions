package com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.zxing.integration.android.IntentIntegrator
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentRaceDetailBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Athlete
import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.fragment.adapter.RaceDetailAdapter
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.RaceDetailViewModel
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.RaceDetailState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.SortingScreenState
import com.jaroapps.stopwatchforcompetitions.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RaceDetailFragment : Fragment(R.layout.fragment_race_detail) {

    private var _binding: FragmentRaceDetailBinding? = null
    private val binding get() = _binding!!
    private val raceDetailAdapter =
        RaceDetailAdapter(object : RaceDetailAdapter.AthleteClickListener {
            override fun onAthleteClick(athlete: Athlete) {
                viewModel.toggleLapDetail(athlete)
            }

            override fun onAthleteNumberClick(athlete: Athlete) {
                editAthleteNumber(athlete)
            }
        })
    private var startRaceData = 0L
    private var athleteForChangeNumber: Athlete? = null
    private val viewModel: RaceDetailViewModel by viewModels()

    private val qrScanLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val scanResult =
                    IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                if (scanResult.contents == null) {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Scanned: " + scanResult.contents,
                        Toast.LENGTH_LONG
                    ).show()
                    // Отобразите сканированное значение в TextView
                    binding.newAthleteNumberEt.setText(scanResult.contents)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRaceDetailBinding.bind(view)

        setRaceDetailAdapter()
        viewModel.getRaceInfo()
        setClickListeners()

        viewModel.raceDetailState.observe(viewLifecycleOwner) {
            renderRaceDetail(it)
        }

        viewModel.sortingScreenState.observe(viewLifecycleOwner) {
            renderSortingScreen(it)
        }

        viewModel.numberAvailable.observe(viewLifecycleOwner) {
            renderNewAthleteNumber(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

            applyNewAthleteNumber.setOnClickListener {
                viewModel.checkNumberAvailability(newAthleteNumberEt.text.toString())
            }

            overlayEditAthleteNumber.setOnClickListener {
                overlayEditAthleteNumber.visibility = View.GONE
                editAthleteNumberGroup.visibility = View.GONE
                binding.newAthleteNumberEt.setText("")
            }

            qrScanner.setOnClickListener {
                val integrator = IntentIntegrator(requireActivity())
                integrator.setPrompt("Scan a QR code")
                integrator.setOrientationLocked(false)
                integrator.setBeepEnabled(false)
                integrator.captureActivity = PortraitCaptureActivity::class.java
                val intent = integrator.createScanIntent()
                qrScanLauncher.launch(intent)
            }
        }
    }

    private fun setRaceDetailAdapter() {
        binding.raceDetailRv.adapter = raceDetailAdapter
        val itemAnimator = binding.raceDetailRv.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun editAthleteNumber(athlete: Athlete) {
        binding.overlayEditAthleteNumber.visibility = View.VISIBLE
        binding.editAthleteNumberGroup.visibility = View.VISIBLE
        athleteForChangeNumber = athlete
    }

    private fun renderRaceDetail(raceInfo: RaceDetailState) {
        if (raceInfo is RaceDetailState.Content) {
            startRaceData = raceInfo.race.startTime
            with(binding) {
                raceDetailAdapter.updateRaceDetailAdapter(raceInfo.athleteList, raceInfo.race)
                startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                startTime.text = Util.convertLongToTime(raceInfo.race.startTime)
                raceName.text = raceInfo.race.name
                lapDistance.text = raceInfo.race.lapDistance.toString()
                totalLapInRace.text = raceInfo.race.totalLapsInRace.toString()
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

    private fun renderNewAthleteNumber(numberAvailable: Boolean) {
        if (numberAvailable) {
            athleteForChangeNumber?.let {
                viewModel.changeAthleteNumber(it)
            }
            binding.overlayEditAthleteNumber.visibility = View.GONE
            binding.editAthleteNumberGroup.visibility = View.GONE
            binding.newAthleteNumberEt.setText("")
        } else {
            showToast("Такой номер уже есть в гонке, введите новый номер.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
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