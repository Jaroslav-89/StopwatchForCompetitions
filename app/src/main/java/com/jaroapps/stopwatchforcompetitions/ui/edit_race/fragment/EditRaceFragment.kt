package com.jaroapps.stopwatchforcompetitions.ui.edit_race.fragment

import android.content.Context
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentEditRaceBinding
import com.jaroapps.stopwatchforcompetitions.domain.model.Race
import com.jaroapps.stopwatchforcompetitions.ui.edit_race.view_model.EditRaceViewModel
import com.jaroapps.stopwatchforcompetitions.util.Util.convertLongToDate
import com.jaroapps.stopwatchforcompetitions.util.Util.convertLongToTime
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditRaceFragment : Fragment(R.layout.fragment_edit_race) {

    private var _binding: FragmentEditRaceBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val args: EditRaceFragmentArgs by navArgs()
    private val viewModel: EditRaceViewModel by viewModels()

    private val keyboard by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                viewModel.editPlaceHolderImg(imageUri.toString())
            }
        }

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                viewModel.editPlaceHolderImg(imageUri.toString())
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditRaceBinding.bind(view)

        viewModel.getRaceByData(args.startRaceDataEditRace)

        viewModel.editRacesState.observe(viewLifecycleOwner) {
            renderState(it)
        }

        setClickListeners()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    alertDialog(BACK)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            hideKeyboard()
            alertDialog(BACK)
        }

        binding.deleteRaceBtn.setOnClickListener {
            alertDialog(DELETE_RACE)
        }

        binding.addRaceImg.setOnClickListener {
            saveEditTextValueViewModel()
            showImageSourceDialog()
        }

        binding.deleteImgBtn.setOnClickListener {
            hideKeyboard()
            alertDialog(DELETE_IMG)
        }

        binding.doneBtn.setOnClickListener {
            hideKeyboard()
            saveEditTextValueViewModel()
            saveRaceAndRedirect()
        }
    }

    private fun saveRaceAndRedirect() {
        alertDialog(SAVE)
    }

    private fun saveEditTextValueViewModel() {
        val distance =
            if (binding.lapDistance.text.isBlank()) "0"
            else
                binding.lapDistance.text.toString()
        val totalLapInRace =
            if (binding.totalLapInRace.text.isBlank()) "0"
            else
                binding.totalLapInRace.text.toString()
        viewModel.editNameText(binding.raceName.text.toString())
        viewModel.editLapDistance(distance)
        viewModel.editTotalLapInRace(totalLapInRace)
        viewModel.editDescriptionText(binding.raceDescription.text.toString())
    }

    private fun hideKeyboard() {
        binding.raceName.clearFocus()
        binding.lapDistance.clearFocus()
        binding.raceDescription.clearFocus()
        keyboard.hideSoftInputFromWindow(binding.raceName.windowToken, 0)
        keyboard.hideSoftInputFromWindow(binding.lapDistance.windowToken, 0)
        keyboard.hideSoftInputFromWindow(binding.raceDescription.windowToken, 0)
    }

    private fun showImageSourceDialog() {
        // Создаем диалоговое окно для выбора между камерой и галереей
        val options = arrayOf("Сделать фото", "Выбрать из галереи")
        val builder = AlertDialog.Builder(requireContext())
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePicture() // Сделать фото
                1 -> pickImageFromGallery() // Выбрать из галереи
            }
        }
        builder.show()
    }

    private fun takePicture() {
        // Создаем файл и Uri для сохранения фотографии
        imageUri = createImageUri()
        imageUri?.let { uri ->
            takePhotoLauncher.launch(uri)
        }
    }

    private fun createImageUri(): Uri? {
        // Создаем файловое имя с помощью текущего времени, чтобы избежать дублирования
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        // Получаем хранилище для фотографий
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            // Создаем файл изображения
            val file = File.createTempFile(imageFileName, ".jpg", storageDir).apply {
                imageUri = Uri.fromFile(this)
            }
            //   Получаем URI для файла с помощью FileProvider
            FileProvider.getUriForFile(
                requireContext(),
                FILE_PROVIDER,
                file
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun pickImageFromGallery() {
        // Запускаем выбор изображения из галереи
        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun setImgFromPlaceHolder(uri: String) {
        if (uri.isNotBlank()) {
            binding.deleteImgBtn.visibility = View.VISIBLE
        } else {
            binding.deleteImgBtn.visibility = View.GONE
        }
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

    private fun alertDialog(state: String) {
        var title = ""
        var message = ""
        var positive = ""
        var negative = ""

        when (state) {
            SAVE -> {
                title = requireContext().getString(R.string.title_save)
                message = requireContext().getString(R.string.message_save)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            BACK -> {
                title = requireContext().getString(R.string.title_back)
                message = requireContext().getString(R.string.message_back)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            DELETE_RACE -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_race)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }

            DELETE_IMG -> {
                title = requireContext().getString(R.string.title_delete)
                message = requireContext().getString(R.string.message_delete_img)
                positive = requireContext().getString(R.string.answer_positive)
                negative = requireContext().getString(R.string.answer_negative)
            }
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positive) { dialogInterface: DialogInterface, i: Int ->
            when (state) {
                SAVE -> {
                    viewModel.changeRace()
                    findNavController().popBackStack()
                }

                BACK -> {
                    findNavController().popBackStack()
                }

                DELETE_RACE -> {
                    viewModel.deleteRace()
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }

                DELETE_IMG -> {
                    viewModel.deleteRaceImg()
                }
            }
        }

        builder.setNegativeButton(negative) { dialogInterface: DialogInterface, i: Int ->
            return@setNegativeButton
        }

        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        positiveButton.setTextColor(requireContext().getColor(R.color.dark_gray_day_night))

        val negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        negativeButton.setTextColor(requireContext().getColor(R.color.dark_gray_day_night))
    }

    private fun renderState(race: Race) {
        with(binding) {
            startDate.text = convertLongToDate(race.startTime)
            startTime.text = convertLongToTime(race.startTime)
            raceName.setText(race.name)
            raceName.setSelection(raceName.length())
            raceDescription.setText(race.description)
            raceDescription.setSelection(raceDescription.length())
            lapDistance.setText(race.lapDistance.toString())
            lapDistance.setSelection(lapDistance.length())
            totalLapInRace.setText(race.totalLapsInRace.toString())
            totalLapInRace.setSelection(totalLapInRace.length())
            setImgFromPlaceHolder(race.imgUrl)
            if (race.isStarted)
                binding.deleteRaceBtn.visibility = View.GONE
            else
                binding.deleteRaceBtn.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val FILE_PROVIDER = "com.jaroapps.stopwatchforcompetitions.fileprovider"
        private const val DELETE_RACE = "delete_race"
        private const val DELETE_IMG = "delete_img"
        private const val BACK = "back"
        private const val SAVE = "save"
    }
}