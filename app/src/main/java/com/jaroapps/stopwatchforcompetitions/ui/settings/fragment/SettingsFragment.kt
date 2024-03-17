package com.jaroapps.stopwatchforcompetitions.ui.settings.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentSettingsBinding
import com.jaroapps.stopwatchforcompetitions.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        checkTheme()
        setClickListeners()

        viewModel.themeSwitcherState.observe(viewLifecycleOwner) {
            renderState(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkTheme() {
        viewModel.checkTheme()
    }

    private fun setClickListeners() {
        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateThemeSettings(isChecked)
        }
        binding.settingsShareBtn.setOnClickListener { viewModel.shareApp() }
        binding.settingsSupportBtn.setOnClickListener { viewModel.contactSupport() }
        binding.privacyPolicyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_privacyPolicyFragment)
        }
    }

    private fun renderState(darkMode: Boolean) {
        binding.themeSwitcher.isChecked = darkMode
    }
}