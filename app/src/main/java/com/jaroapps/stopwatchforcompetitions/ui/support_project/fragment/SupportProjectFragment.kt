package com.jaroapps.stopwatchforcompetitions.ui.support_project.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentSupportProjectBinding
import com.jaroapps.stopwatchforcompetitions.ui.support_project.view_model.SupportProjectViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SupportProjectFragment : Fragment() {

    private lateinit var binding: FragmentSupportProjectBinding
    private val viewModel: SupportProjectViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()

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