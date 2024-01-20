package com.example.stopwatchforcompetitions.ui.stopwatch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.FragmentStopwatchBinding
import com.example.stopwatchforcompetitions.ui.stopwatch.fragment.adapter.FastResultAdapter
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.StopwatchViewModel
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state.AddAthleteNumberState
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state.FastResultState
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.state.TimerState
import org.koin.androidx.viewmodel.ext.android.viewModel

class StopwatchFragment : Fragment() {

    private lateinit var binding: FragmentStopwatchBinding
    private val fastResultAdapter = FastResultAdapter()
    private var raceWasStarted = false
    private val viewModel: StopwatchViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFastResultAdapter()
        checkRaceHasBeenStarted()
        setTextViewClickListeners()
        setAddBtnClickListeners()
        setNumberClickListener()

        viewModel.timerState.observe(viewLifecycleOwner) {
            renderTimer(it)
        }

        viewModel.fastResultState.observe(viewLifecycleOwner) {
            renderFastResult(it)
        }

        viewModel.addAthleteNumberState.observe(viewLifecycleOwner) {
            renderAthleteNumberState(it)
        }
    }

    private fun setFastResultAdapter() {
        binding.fastResultRv.adapter = fastResultAdapter
    }

    private fun checkRaceHasBeenStarted() {
        viewModel.checkRaceHasBeenStarted()
    }

    private fun setTextViewClickListeners() {
        binding.startStopBtn.setOnClickListener {
            viewModel.onPlayStopButtonClicked()
            if (raceWasStarted) {
                val action =
                    StopwatchFragmentDirections.actionStopwatchToSaveRaceFragment(0)
                findNavController().navigate(action)
            }
        }

        binding.raceDetailBtn.setOnClickListener {
            if (raceWasStarted) {
                findNavController().navigate(R.id.action_stopwatch_to_raceDetailFragment)
            } else {
                findNavController().navigate(R.id.action_stopwatch_to_allRacesFragment)
            }
        }

        binding.athleteNumberAddTx1.setOnClickListener {
            viewModel.changeTextViewFocus(FIRST_EDIT_TEXT)
        }

        binding.athleteNumberAddTx2.setOnClickListener {
            viewModel.changeTextViewFocus(SECOND_EDIT_TEXT)
        }

        binding.athleteNumberAddTx3.setOnClickListener {
            viewModel.changeTextViewFocus(THIRD_EDIT_TEXT)
        }

        binding.athleteNumberAddTx4.setOnClickListener {
            viewModel.changeTextViewFocus(FOURTH_EDIT_TEXT)
        }
    }

    private fun setAddBtnClickListeners() {
        binding.athleteNumberAddBtn1.setOnClickListener {
            viewModel.addAthleteResult(1)
        }

        binding.athleteNumberAddBtn2.setOnClickListener {
            viewModel.addAthleteResult(2)
        }

        binding.athleteNumberAddBtn3.setOnClickListener {
            viewModel.addAthleteResult(3)
        }

        binding.athleteNumberAddBtn4.setOnClickListener {
            viewModel.addAthleteResult(4)
        }
    }

    private fun setNumberClickListener() {
        binding.figureOne.setOnClickListener {
            viewModel.changeTextViewText("1")
        }

        binding.figureTwo.setOnClickListener {
            viewModel.changeTextViewText("2")
        }

        binding.figureThree.setOnClickListener {
            viewModel.changeTextViewText("3")
        }

        binding.figureFour.setOnClickListener {
            viewModel.changeTextViewText("4")
        }

        binding.figureFive.setOnClickListener {
            viewModel.changeTextViewText("5")
        }

        binding.figureSix.setOnClickListener {
            viewModel.changeTextViewText("6")
        }

        binding.figureSeven.setOnClickListener {
            viewModel.changeTextViewText("7")
        }

        binding.figureEight.setOnClickListener {
            viewModel.changeTextViewText("8")
        }

        binding.figureNine.setOnClickListener {
            viewModel.changeTextViewText("9")
        }

        binding.figureZero.setOnClickListener {
            viewModel.changeTextViewText("0")
        }

        binding.menu.setOnClickListener {
        }

        binding.del.setOnClickListener {
            viewModel.changeTextViewText(DELETE)
        }
    }

    private fun renderTimer(state: TimerState) {
        if (state is TimerState.IsStarted) {
            raceWasStarted = true
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            binding.timerTv.text = state.time
            showStopBtn()
            showRaceDetailBtn()
        } else {
            raceWasStarted = false
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            binding.timerTv.text = requireContext().getString(R.string.default_timer)
            showStartBtn()
            showMenuBtn()
        }
    }

    private fun renderFastResult(athletesResults: FastResultState) {
        if (athletesResults is FastResultState.Content) {
            fastResultAdapter.updateFastResultAdapter(athletesResults.athletesList)
        }
    }

    private fun renderAthleteNumberState(state: AddAthleteNumberState) {
        if (state is AddAthleteNumberState.IsStarted) {
            showAddAthleteNumberContent(state)
        } else {
            showAddAthleteNumberDefault()
        }
    }

    private fun showStartBtn() {
        binding.startStopBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.green
            )
        )
        binding.startStopBtn.setIconResource(R.drawable.ic_start)
    }

    private fun showStopBtn() {
        binding.startStopBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        binding.startStopBtn.setIconResource(R.drawable.ic_stop)
    }

    private fun showRaceDetailBtn() {
        binding.raceDetailBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue
            )
        )
        binding.raceDetailBtn.setIconResource(R.drawable.ic_laps_detail)
    }

    private fun showMenuBtn() {
        binding.raceDetailBtn.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.blue
            )
        )
        binding.raceDetailBtn.setIconResource(R.drawable.ic_menu)
    }

    private fun showAddAthleteNumberContent(state: AddAthleteNumberState.IsStarted) {
        resetAddAthleteBackground()

        when (state.numberOfTextView) {
            FIRST_EDIT_TEXT -> {
                binding.addAthleteNumberFieldOne.setBackgroundResource(R.drawable.bg_add_athlete_number_field)
            }

            SECOND_EDIT_TEXT -> {
                binding.addAthleteNumberFieldTwo.setBackgroundResource(R.drawable.bg_add_athlete_number_field)
            }

            THIRD_EDIT_TEXT -> {
                binding.addAthleteNumberFieldTree.setBackgroundResource(R.drawable.bg_add_athlete_number_field)
            }

            FOURTH_EDIT_TEXT -> {
                binding.addAthleteNumberFieldFour.setBackgroundResource(R.drawable.bg_add_athlete_number_field)
            }
        }
        binding.athleteNumberAddTx1.text = state.valueOfTextViewOne
        binding.athleteNumberAddTx2.text = state.valueOfTextViewTwo
        binding.athleteNumberAddTx3.text = state.valueOfTextViewThree
        binding.athleteNumberAddTx4.text = state.valueOfTextViewFour
    }

    private fun showAddAthleteNumberDefault() {
        resetAddAthleteBackground()
        binding.athleteNumberAddTx1.text = ""
        binding.athleteNumberAddTx2.text = ""
        binding.athleteNumberAddTx3.text = ""
        binding.athleteNumberAddTx4.text = ""
    }

    private fun resetAddAthleteBackground() {
        binding.addAthleteNumberFieldOne.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldTwo.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldTree.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldFour.setBackgroundResource(R.color.white)
    }

    companion object {
        const val FIRST_EDIT_TEXT = 1
        const val SECOND_EDIT_TEXT = 2
        const val THIRD_EDIT_TEXT = 3
        const val FOURTH_EDIT_TEXT = 4
        const val DELETE = "delete"
    }
}