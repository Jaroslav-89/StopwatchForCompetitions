package com.jaroapps.stopwatchforcompetitions.ui.all_races.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentAllRacesBinding
import com.jaroapps.stopwatchforcompetitions.ui.all_races.fragment.adapter.AllRacesAdapter
import com.jaroapps.stopwatchforcompetitions.ui.all_races.view_model.AllRacesViewModel
import com.jaroapps.stopwatchforcompetitions.ui.all_races.view_model.state.AllRaceState
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllRacesFragment : Fragment(R.layout.fragment_all_races) {

    private var _binding: FragmentAllRacesBinding? = null
    private val binding get() = _binding!!
    private val allRacesAdapter =
        AllRacesAdapter {
            val argument = it.startTime
            val action =
                AllRacesFragmentDirections.actionAllRacesFragmentToSaveRaceFragment(argument)
            findNavController().navigate(action)
        }
    private val viewModel: AllRacesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllRacesBinding.bind(view)

        setAdapter()
        viewModel.loadRaceHistory()
        setClickListeners()

        viewModel.allRacesState.observe(viewLifecycleOwner) {
            renderRaceList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAdapter() {
        binding.allRacesRv.adapter = allRacesAdapter
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.settingsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_allRacesFragment_to_settingsFragment)
        }
    }

    private fun renderRaceList(state: AllRaceState) {
        when (state) {
            is AllRaceState.Content -> {
                binding.allRacesRv.visibility = View.VISIBLE
                binding.raceHistoryPlaceholder.visibility = View.GONE
                binding.raceHistoryPlaceholderText.visibility = View.GONE
                allRacesAdapter.updateFastResultAdapter(state.raceList)
            }

            is AllRaceState.Empty -> {
                binding.allRacesRv.visibility = View.GONE
                binding.raceHistoryPlaceholder.visibility = View.VISIBLE
                binding.raceHistoryPlaceholderText.visibility = View.VISIBLE
                allRacesAdapter.updateFastResultAdapter(emptyList())
            }
        }
    }
}