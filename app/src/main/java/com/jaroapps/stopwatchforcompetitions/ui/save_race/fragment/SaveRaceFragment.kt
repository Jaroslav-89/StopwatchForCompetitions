package com.jaroapps.stopwatchforcompetitions.ui.save_race.fragment

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentSaveRaceBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.SortingState
import com.jaroapps.stopwatchforcompetitions.ui.race_detail.view_model.state.SortingScreenState
import com.jaroapps.stopwatchforcompetitions.ui.save_race.fragment.adapter.SaveRaceAdapter
import com.jaroapps.stopwatchforcompetitions.ui.save_race.view_model.SaveRaceViewModel
import com.jaroapps.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import com.jaroapps.stopwatchforcompetitions.util.Util
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class SaveRaceFragment : Fragment(R.layout.fragment_save_race) {

    private var _binding: FragmentSaveRaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var vibrator: Vibrator
    private lateinit var raceResultBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val args: SaveRaceFragmentArgs by navArgs()
    private var startRaceData = 0L

    private val viewModel: SaveRaceViewModel by viewModel()
    private val saveRaceAdapter = SaveRaceAdapter {
        viewModel.toggleLapDetail(it)
    }

    private val saveLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument(MIME_TYPE_EXEL)) { uri ->
            try {
                uri?.let {
                    viewModel.saveResultInXls(uri)
                    showToast(getString(R.string.save_msg) + uri.path)
                }
            } catch (e: Exception) {
                Log.d("e", e.toString())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRaceBinding.bind(view)
        binding.root.doOnNextLayout {
            calculatePeekHeight()
        }

        viewModel.getRaceInfo(args.startRaceDataSaveRace)
        initBottomSheets()
        setClickListeners()

        viewModel.saveRacesState.observe(viewLifecycleOwner) {
            renderRaceInformation(it)
        }

        viewModel.sortingScreenState.observe(viewLifecycleOwner) {
            renderSortingScreen(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheets() {
        raceResultBottomSheetBehavior = BottomSheetBehavior.from(binding.resultBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        raceResultBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = abs(slideOffset)
                binding.sortingTv.alpha = abs(slideOffset)
                binding.sortingIc.alpha = abs(slideOffset)

                if (slideOffset > 0.1) {
                    binding.sortingTv.visibility = View.VISIBLE
                    binding.sortingIc.visibility = View.VISIBLE
                } else {
                    binding.sortingTv.visibility = View.GONE
                    binding.sortingIc.visibility = View.GONE
                }
            }
        })

        binding.saveRaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.saveRaceRv.adapter = saveRaceAdapter
        val itemAnimator = binding.saveRaceRv.itemAnimator
        if (itemAnimator is DefaultItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    @SuppressLint("Recycle")
    private fun setClickListeners() {
        with(binding) {
            editBtn.setOnClickListener {
                val argument = startRaceData
                val action =
                    SaveRaceFragmentDirections.actionSaveRaceFragmentToEditRaceFragment(argument)
                findNavController().navigate(action)
            }

            backBtn.setOnClickListener {
                findNavController().navigateUp()
            }

            addRaceInFavoriteBtn.setOnClickListener {
                viewModel.toggleFavoriteBtn()
            }

            saveXlsBtn.setOnClickListener {
                saveLauncher.launch(getString(R.string.xls_name))
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

            overlaySorting.setOnClickListener {
                viewModel.toggleSortingBtn()
            }
        }
    }

    private fun renderRaceInformation(raceInfo: SaveRaceState) {
        if (raceInfo is SaveRaceState.Content) {
            if (raceInfo.race != null) {
                startRaceData = raceInfo.race.startTime
                with(binding) {
                    setImgFromPlaceHolder(raceInfo.race.imgUrl)
                    addRaceInFavoriteBtn.setImageDrawable(getFavoriteToggleDrawable(raceInfo.race.isFavorite))
                    startDate.text = Util.convertLongToDate(raceInfo.race.startTime)
                    startTime.text = Util.convertLongToTime(raceInfo.race.startTime)
                    raceName.text = raceInfo.race.name
                    numberOfAthletes.text = raceInfo.race.athletes.size.toString()
                    lapDistance.text = raceInfo.race.lapDistance.toString()
                    totalLapInRace.text = raceInfo.race.totalLapsInRace.toString()
                    raceDescription.text = raceInfo.race.description
                    if (raceInfo.athleteList.isNotEmpty()) {
                        placeholderMessage.visibility = View.GONE
                        saveRaceRv.visibility = View.VISIBLE
                        saveRaceAdapter.updateRaceDetailAdapter(raceInfo.athleteList, raceInfo.race)
                    } else {
                        placeholderMessage.visibility = View.VISIBLE
                        saveRaceRv.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun renderSortingScreen(sortingScreenState: SortingScreenState) {
        when (sortingScreenState) {
            is SortingScreenState.Gone -> {
                binding.overlaySorting.visibility = View.GONE
                binding.sortingGroup.visibility = View.GONE
                binding.resultBottomSheet.visibility = View.VISIBLE
                binding.sortingTv.text = getSortingText(sortingScreenState.sortingState)
            }

            is SortingScreenState.Visible -> {
                binding.resultBottomSheet.visibility = View.GONE
                binding.overlaySorting.visibility = View.VISIBLE
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

    private fun getFavoriteToggleDrawable(isFavorite: Boolean?): Drawable? {
        return if (isFavorite == null || !isFavorite) {
            getDrawable(requireContext(), R.drawable.ic_inactive_favorite_save_race)
        } else {
            getDrawable(requireContext(), R.drawable.ic_active_favorite__save_race)
        }
    }

    private fun showToast(msg: String) {
        vibrate()
        Toast.makeText(
            requireContext(),
            msg,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun vibrate() {
        vibrator = requireActivity().getSystemService()!!
        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
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

    private fun calculatePeekHeight() {
        val screenHeight = binding.root.height
        raceResultBottomSheetBehavior.peekHeight =
            (screenHeight - binding.numberOfAthletesHeading.bottom - BS_OFFSET_PX).coerceAtLeast(
                BS_MIN_SIZE_PX
            )
        raceResultBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    companion object {
        private const val BS_OFFSET_PX = 24
        private const val BS_MIN_SIZE_PX = 200
        private const val MIME_TYPE_EXEL = "application/vnd.ms-excel"
    }
}