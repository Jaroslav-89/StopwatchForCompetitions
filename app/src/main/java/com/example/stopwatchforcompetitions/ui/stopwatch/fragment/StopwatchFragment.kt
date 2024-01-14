package com.example.stopwatchforcompetitions.ui.stopwatch.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stopwatchforcompetitions.R
import com.example.stopwatchforcompetitions.databinding.FragmentStopwatchBinding
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.AddAthleteNumberState
import com.example.stopwatchforcompetitions.ui.stopwatch.view_model.StopwatchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StopwatchFragment : Fragment() {

    private lateinit var binding: FragmentStopwatchBinding
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

        setClickListeners()

        viewModel.addAthleteNumberState.observe(viewLifecycleOwner) {
            renderAthleteNumberState(it)
        }
    }

    private fun renderAthleteNumberState(state: AddAthleteNumberState) {
        binding.addAthleteNumberFieldOne.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldTwo.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldTree.setBackgroundResource(R.color.white)
        binding.addAthleteNumberFieldFour.setBackgroundResource(R.color.white)

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

    private fun setClickListeners() {
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

    companion object {
        private const val FIRST_EDIT_TEXT = 1
        private const val SECOND_EDIT_TEXT = 2
        private const val THIRD_EDIT_TEXT = 3
        private const val FOURTH_EDIT_TEXT = 4
        const val DELETE = "delete"
    }
}