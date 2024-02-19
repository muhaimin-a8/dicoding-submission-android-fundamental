package me.muhaimin.githubuser.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.muhaimin.githubuser.databinding.ModalSettingDialogBinding
import me.muhaimin.githubuser.factory.ViewModelFactory

class ModalBottomSettingDialog: BottomSheetDialogFragment() {
    private lateinit var binding: ModalSettingDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val factory = ViewModelFactory.getInstance(requireContext())
        val viewModel by viewModels<ModalBottomSettingViewModel> { factory }

        binding = ModalSettingDialogBinding.inflate(inflater, container, false)

        viewModel.getThemeSettings().observe(viewLifecycleOwner) {isDarkMode ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        return binding.root
    }
}