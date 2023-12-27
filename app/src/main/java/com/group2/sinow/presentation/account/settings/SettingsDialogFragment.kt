package com.group2.sinow.presentation.account.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group2.sinow.databinding.FragmentSettingsDialogBinding
import com.group2.sinow.presentation.account.AccountFeatureViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSettingsDialogBinding

    private val viewModel: SettingsViewModel by viewModel()

    private val accountFeatureViewModel: AccountFeatureViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeDarkModePref()
        setSwitchAction()
    }

    private fun observeDarkModePref() {
        lifecycleScope.launch {
            binding.swDarkMode.isChecked = accountFeatureViewModel.userDarkMode.firstOrNull() ?: false
        }
    }

    private fun setSwitchAction() {
        binding.swDarkMode.setOnCheckedChangeListener { _, isUsingDarkMode ->
            viewModel.setUserDarkModePref(isUsingDarkMode)
        }
    }


}