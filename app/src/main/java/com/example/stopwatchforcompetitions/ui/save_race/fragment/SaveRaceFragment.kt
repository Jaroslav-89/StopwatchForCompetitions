package com.example.stopwatchforcompetitions.ui.save_race.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.FragmentSaveRaceBinding
import com.example.stopwatchforcompetitions.ui.save_race.fragment.adapter.SaveRaceAdapter
import com.example.stopwatchforcompetitions.ui.save_race.view_model.SaveRaceViewModel
import com.example.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import com.example.stopwatchforcompetitions.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaveRaceFragment : Fragment() {
    private lateinit var binding: FragmentSaveRaceBinding
    private val args: SaveRaceFragmentArgs by navArgs()
    private var startRaceData = 0L
    private var resultIsVisible = false
    private val viewModel: SaveRaceViewModel by viewModel()
    private val saveRaceAdapter = SaveRaceAdapter {
        viewModel.toggleLapDetail(it)
    }

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

        setSaveRaceAdapter()

        setClickListeners()



        viewModel.saveRacesState.observe(viewLifecycleOwner) {
            renderRaceInformation(it)
        }
    }

    private fun setSaveRaceAdapter() {
        binding.saveRaceRv.adapter = saveRaceAdapter
    }

    private fun setClickListeners() {
        binding.editBtn.setOnClickListener {
            val argument = startRaceData
            val action =
                SaveRaceFragmentDirections.actionSaveRaceFragmentToEditRaceFragment(argument)
            findNavController().navigate(action)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.raceResultBtn.setOnClickListener {
            if (resultIsVisible) {
                resultIsVisible = false
                binding.arrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_down))
                binding.raceResultGroup.visibility = View.GONE
                binding.athleteHeadingDivider.visibility = View.GONE
                binding.saveRaceRv.visibility = View.GONE
            } else {
                resultIsVisible = true
                binding.arrow.setImageDrawable(requireContext().getDrawable(R.drawable.ic_arrow_up))
                binding.raceResultGroup.visibility = View.VISIBLE
                binding.athleteHeadingDivider.visibility = View.VISIBLE
                binding.saveRaceRv.visibility = View.VISIBLE
            }
        }
    }

    private fun renderRaceInformation(raceInfo: SaveRaceState) {
        if (raceInfo is SaveRaceState.Content) {
            if (raceInfo.race != null) {
                startRaceData = raceInfo.race.startTime
                with(binding) {
                    setImgFromPlaceHolder(raceInfo.race.imgUrl)
                    startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                    startTime.text = Util.convertLongToTime(raceInfo.race.startTime)
                    raceName.text = raceInfo.race.name
                    numberOfAthletes.text = raceInfo.race.athletes.size.toString()
                    lapDistance.text = raceInfo.race.lapDistance.toString()
                    raceDescription.text = raceInfo.race.description
                    saveRaceAdapter.updateRaceDetailAdapter(raceInfo.athleteList, raceInfo.race)
                }
            }
        }
    }

    private fun setImgFromPlaceHolder(uri: String) {

        val newUri = uri.toUri()
        Glide.with(this)
            .load(newUri)
            .placeholder(R.drawable.img_competition_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.corner_radius)
                ),
            )
            .into(binding.competitionImage)
    }
}