package com.jaroapps.stopwatchforcompetitions.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jaroapps.stopwatchforcompetitions.R
import com.jaroapps.stopwatchforcompetitions.databinding.FragmentSettingsBinding
import com.jaroapps.stopwatchforcompetitions.ui.settings.view_model.SettingsViewModel
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
        binding.privacyPolicyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_privacyPolicyFragment)
        }
        binding.supportProjectBtn.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_supportProjectFragment)
        }
    }

    private fun renderState(darkMode: Boolean) {
        binding.themeSwitcher.isChecked = darkMode
    }
}