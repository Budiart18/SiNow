package com.group2.sinow.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.group2.sinow.databinding.ActivityAccountFeatureBinding
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.auth.register.RegisterActivity
import com.group2.sinow.presentation.change_password.ChangePasswordUserActivity
import com.group2.sinow.presentation.payment_history.PaymentHistoryActivity
import com.group2.sinow.presentation.profile.ProfileActivity
import com.group2.sinow.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFeatureActivity : AppCompatActivity() {

    private val binding : ActivityAccountFeatureBinding by lazy {
        ActivityAccountFeatureBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
        getData()
        observeData()
    }

    private fun observeData() {
        viewModel.userData.observe(this){
            if (it.payload != null) {
                binding.cvAccount.isVisible = true
                binding.layoutNonloginAccount.root.isVisible = false
            } else {
                binding.cvAccount.isVisible = false
                binding.layoutNonloginAccount.root.isVisible = true
            }
        }
    }

    private fun getData() {
        viewModel.getUserData()
    }

    private fun setClickListener() {
        binding.tvProfileTitle.setOnClickListener {
            navigateToProfile()
        }
        binding.tvChangePasswordTitle.setOnClickListener {
            navigateToChangePassword()
        }
        binding.tvPaymentHistoryTitle.setOnClickListener {
            navigateToPaymentHistory()
        }
        binding.tvLogoutTitle.setOnClickListener {
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
    }

    private fun doLogout() {
        val dialog = AlertDialog.Builder(this).setMessage("Apakah kamu yakin ingin keluar akun ?")
            .setPositiveButton(
                "Ya"
            ) { _, _ ->
                viewModel.doLogout()
                navigateToLogin()
            }
            .setNegativeButton(
                "Tidak"
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
        startActivity(Intent(this, PaymentHistoryActivity::class.java))
    }

    private fun navigateToChangePassword() {
        startActivity(Intent(this, ChangePasswordUserActivity::class.java))
    }

    private fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }
}