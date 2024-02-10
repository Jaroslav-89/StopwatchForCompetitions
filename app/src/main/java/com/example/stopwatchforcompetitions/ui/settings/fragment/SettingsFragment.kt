package com.example.stopwatchforcompetitions.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stopwatchforcompetitions.databinding.FragmentSettingsBinding
import com.example.stopwatchforcompetitions.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkTheme()
        setClickListeners()

        viewModel.themeSwitcherState.observe(viewLifecycleOwner) {
            renderState(it)
        }
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
        binding.settingsUserAgreementBtn.setOnClickListener { viewModel.openTerms() }
    }

    private fun renderState(darkMode: Boolean) {
        binding.themeSwitcher.isChecked = darkMode
    }
}