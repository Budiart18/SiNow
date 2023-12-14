package com.group2.sinow.presentation.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.group2.sinow.R
import com.group2.sinow.data.network.api.model.register.RegisterRequest
import com.group2.sinow.databinding.ActivityRegisterBinding
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.auth.otp.OTPActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.highLightWord
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
        observeResult()
    }

    private fun observeResult() {
        lifecycleScope.launchWhenStarted {
            viewModel.registerState.collect { resultWrapper ->
                resultWrapper.proceedWhen(
                    doOnSuccess = {
                        binding.progressbar.isVisible = false
                        binding.tvErrorMessage.isVisible = false
                        binding.btnRegister.isVisible  = true
                        navigateToOTP()
                    }, doOnLoading = {
                        binding.btnRegister.isVisible = true
                        binding.progressbar.isVisible = true
                        binding.tvErrorMessage.isVisible = false

                    }, doOnError = {
                        binding.progressbar.isVisible = false
                        binding.tvErrorMessage.isVisible = true
                        if (it.exception is ApiException){
                            binding.tvErrorMessage.text = it.exception.getParsedError()?.message
                        }

                    })
            }
        }
    }


    private fun navigateToOTP() {
        val email = binding.etEmail.text.toString().trim()
        val intent = Intent(this, OTPActivity::class.java).apply {
            putExtra("email", email)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun setClickListener() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvNavigateToLogin.setOnClickListener {
            navigateToLogin()
        }


    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }


    private fun isFormValid(): Boolean {
        val fullName = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phoneNumber = binding.etNoTelp.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        return checkNameValidation(fullName) && checkEmailValidation(email) && checkPhoneNumberValidation(
            phoneNumber
        ) && checkPasswordValidation(password)
    }

    private fun checkNameValidation(fullName: String): Boolean {
        return if (fullName.isEmpty()) {
            binding.etlName.isErrorEnabled = true
            binding.etlName.error = getString(R.string.text_hint_name_empty)
            false
        } else {
            binding.etlName.isErrorEnabled = false
            true
        }
    }

    private fun checkEmailValidation(email: String): Boolean {
        return if (email.isEmpty()) {
            binding.etlEmail.isErrorEnabled = true
            binding.etlEmail.error = getString(R.string.text_error_email_empty)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etlEmail.isErrorEnabled = true
            binding.etlEmail.error = getString(R.string.text_error_email_invalid)
            false
        } else {
            binding.etlEmail.isErrorEnabled = false
            true
        }
    }

    private fun checkPhoneNumberValidation(
        phoneNumber: String,
    ): Boolean {
        return if (phoneNumber.isEmpty()) {
            binding.etlNoTelp.isErrorEnabled = true
            binding.etlNoTelp.error = getString(R.string.text_error_phone_cannot_empty)
            false
        } else if (phoneNumber.length < 10) {
            binding.etlNoTelp.isErrorEnabled = true
            binding.etlNoTelp.error = getString(R.string.text_hint_phonenumber_length)
            false
        } else {
            binding.etlNoTelp.isErrorEnabled = false
            true
        }
    }

    private fun checkPasswordValidation(password: String): Boolean {
        return if (password.isEmpty()) {
            binding.etlPassword.isErrorEnabled = true
            binding.etlPassword.error = getString(R.string.text_hint_password_empty)
            false
        } else if (password.length < 8) {
            binding.etlPassword.isErrorEnabled = true
            binding.etlPassword.error = getString(R.string.text_hint_password_length)
            false
        } else {
            binding.etlPassword.isErrorEnabled = false
            true
        }
    }


    private fun doRegister() {
        if (isFormValid()) {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phoneNumber = binding.etNoTelp.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            val registerRequest = RegisterRequest(name, email, phoneNumber, password)
            viewModel.register(registerRequest)
        }
    }


}