package com.group2.sinow.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityAccountFeatureBinding
import com.group2.sinow.presentation.account.settings.SettingsDialogFragment
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.auth.register.RegisterActivity
import com.group2.sinow.presentation.change_password.ChangePasswordUserActivity
import com.group2.sinow.presentation.transactionhistory.TransactionHistoryActivity
import com.group2.sinow.presentation.profile.ProfileActivity
import com.group2.sinow.presentation.profile.ProfileViewModel
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFeatureActivity : AppCompatActivity() {

    private val binding : ActivityAccountFeatureBinding by lazy {
        ActivityAccountFeatureBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModel()

    private val accountFeatureViewModel: AccountFeatureViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeDarkModePref()
        setClickListener()
        getData()
        observeData()
    }

    private fun observeData() {
        viewModel.userData.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.cvAccount.isVisible = true
                    binding.layoutNonloginAccount.root.isVisible = false
                },
                doOnLoading = {
                    binding.cvAccount.isVisible = false
                    binding.layoutNonloginAccount.root.isVisible = false
                },
                doOnError = {
                    if (it.exception is ApiException) {
                        if (it.exception.httpCode == 401) {
                            binding.cvAccount.isVisible = false
                            binding.layoutNonloginAccount.root.isVisible = true
                        } else {
                            binding.cvAccount.isVisible = false
                            binding.layoutNonloginAccount.root.isVisible = true
                        }
                    }
                }
            )
        }
    }


    private fun getData() {
        viewModel.getUserData()
    }

    private fun setClickListener() {
        binding.llProfile.setOnClickListener {
            navigateToProfile()
        }
        binding.llChangePassword.setOnClickListener {
            navigateToChangePassword()
        }
        binding.llPaymentHistory.setOnClickListener {
            navigateToPaymentHistory()
        }
        binding.llLogout.setOnClickListener {
            doLogout()
        }
        binding.layoutNonloginAccount.btnLogin.setOnClickListener {
            navigateToLogin()
        }
        binding.layoutNonloginAccount.btnRegister.setOnClickListener {
            navigateToRegister()
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.llSettings.setOnClickListener {
            openSettingDialog()
        }
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(this).setMessage(getString(R.string.tv_are_you_sure_want_to_logout))
            .setPositiveButton(
                getString(R.string.tv_yes)
            ) { _, _ ->
                viewModel.doLogout()
                navigateToLogin()
            }
            .setNegativeButton(
                getString(R.string.tv_no)
            ) { _, _ ->
                // no-op , do nothing
            }.create()
        dialog.show()
    }

    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java)).apply {
            Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    private fun navigateToPaymentHistory() {
        startActivity(Intent(this, TransactionHistoryActivity::class.java))
    }

    private fun navigateToChangePassword() {
        startActivity(Intent(this, ChangePasswordUserActivity::class.java))
    }

    private fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
    private fun openSettingDialog() {
        SettingsDialogFragment().show(supportFragmentManager, null )
    }

    private fun observeDarkModePref() {
        accountFeatureViewModel.userDarkModeLiveData.observe(this) { isUsingDarkMode ->
            AppCompatDelegate.setDefaultNightMode(if (isUsingDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}