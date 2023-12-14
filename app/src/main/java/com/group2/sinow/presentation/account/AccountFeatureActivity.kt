package com.group2.sinow.presentation.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityAccountFeatureBinding
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.auth.register.RegisterActivity
import com.group2.sinow.presentation.change_password.ChangePasswordActivity
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
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.tvChangePasswordTitle.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
        binding.tvPaymentHistoryTitle.setOnClickListener {
            startActivity(Intent(this, PaymentHistoryActivity::class.java))
        }
        binding.tvLogoutTitle.setOnClickListener {

        }
        binding.layoutNonloginAccount.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.layoutNonloginAccount.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}