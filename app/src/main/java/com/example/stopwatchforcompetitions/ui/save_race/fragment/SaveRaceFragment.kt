package com.example.stopwatchforcompetitions.ui.save_race.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.FragmentSaveRaceBinding
import com.example.stopwatchforcompetitions.ui.save_race.fragment.adapter.SaveRaceAdapter
import com.example.stopwatchforcompetitions.ui.save_race.view_model.SaveRaceViewModel
import com.example.stopwatchforcompetitions.ui.save_race.view_model.state.SaveRaceState
import com.example.stopwatchforcompetitions.util.Util
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs


class SaveRaceFragment : Fragment() {
    private lateinit var binding: FragmentSaveRaceBinding
    private lateinit var vibrator: Vibrator
    private lateinit var raceResultBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val args: SaveRaceFragmentArgs by navArgs()
    private var startRaceData = 0L

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
        binding.root.doOnNextLayout {
            calculatePeekHeight()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRaceInfo(args.startRaceDataSaveRace)
        initBottomSheets()
        setClickListeners()

        viewModel.saveRacesState.observe(viewLifecycleOwner) {
            renderRaceInformation(it)
        }
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

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = abs(slideOffset)
            }
        })

        binding.saveRaceRv.layoutManager = LinearLayoutManager(requireContext())
        binding.saveRaceRv.adapter = saveRaceAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToast()
                    viewModel.saveResultInXls()
                }
                return
            }

            else -> {
            }
        }
    }

    @SuppressLint("Recycle")
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

        binding.addRaceInFavoriteBtn.setOnClickListener {
            viewModel.toggleFavoriteBtn()
        }

        binding.saveXlsBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT > 29) {
                showToast()
                viewModel.saveResultInXls()
            } else {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requireActivity().requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                    )
                } else {
                    showToast()
                    viewModel.saveResultInXls()
                }
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

    private fun getFavoriteToggleDrawable(isFavorite: Boolean?): Drawable? {
        return if (isFavorite == null || !isFavorite) {
            requireContext().getDrawable(R.drawable.ic_inactive_favorite_save_race)
        } else {
            requireContext().getDrawable(R.drawable.ic_active_favorite__save_race)
        }
    }

    private fun showToast() {
        vibrate()
        val msg = if (Build.VERSION.SDK_INT > 29) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .toString()
        } else {
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()
        }
        Toast.makeText(
            requireContext(),
            getString(R.string.save_msg) + msg, Toast.LENGTH_LONG
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
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200
    }
}