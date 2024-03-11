package com.jaroapps.stopwatchforcompetitions.ui.support_project.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentSupportProjectBinding
import com.jaroapps.stopwatchforcompetitions.ui.support_project.view_model.SupportProjectViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SupportProjectFragment : Fragment(R.layout.fragment_support_project) {


    private var _binding: FragmentSupportProjectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SupportProjectViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSupportProjectBinding.bind(view)

        setClickListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.supportProjectBtn.setOnClickListener {
            viewModel.supportProject()
        }

        binding.rateProjectBtn.setOnClickListener {
            viewModel.rateProject()
        }
    }
}