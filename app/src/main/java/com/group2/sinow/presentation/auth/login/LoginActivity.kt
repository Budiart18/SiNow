package com.group2.sinow.presentation.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.group2.sinow.R
import com.group2.sinow.data.network.api.model.login.LoginRequest
import com.group2.sinow.databinding.ActivityLoginBinding
import com.group2.sinow.presentation.auth.otp.OTPActivity
import com.group2.sinow.presentation.auth.register.RegisterActivity
import com.group2.sinow.presentation.homepage.HomeFragment
import com.group2.sinow.presentation.main.MainActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.highLightWord
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
        observeData()
    }



    private fun observeData() {
        viewModel.loginResult.observe(this){result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateLogin.root.isVisible = false
                    binding.layoutStateLogin.loadingAnimation.isVisible = false
                    binding.tvErrorMessage.isVisible = false
                    it.payload?.data?.token?.let { token -> viewModel.saveToken(token) }
                    navigateToHome()
                },

                doOnLoading = {
                    binding.layoutStateLogin.root.isVisible = true
                    binding.layoutStateLogin.loadingAnimation.isVisible = true
                    binding.tvErrorMessage.isVisible = false

                },

                doOnError = {
                    binding.layoutStateLogin.root.isVisible = false
                    binding.layoutStateLogin.loadingAnimation.isVisible = false
                    binding.tvErrorMessage.isVisible = true
                    if (it.exception is ApiException){
                        binding.tvErrorMessage.text = it.exception.getParsedError()?.message
                    }

                }
            )
        }
        viewModel.userToken.observe(this){token ->
            if (token != null) navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity((intent))
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)
    }

    private fun setOnClickListener() {
        binding.btnLogin.setOnClickListener {
            doLogin()
        }

        binding.tvNavigateToRegister.highLightWord(getString(R.string.text_register_here)){
            navigateToRegister()
        }

        binding.tvLoginWithoutLogin.setOnClickListener {
            navigateToHome()
        }

    }



    private fun doLogin() {
        if(isFormValid()){
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.doLogin(email, password)
        }
    }

    private fun isFormValid(): Boolean {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        return checkEmailValidation(email) && checkPasswordValidation(password)
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

    private fun checkPasswordValidation(
        password: String
    ): Boolean {
        return if (password.isEmpty()) {
            binding.etlPassword.isErrorEnabled = true
            binding.etlPassword.error = getString(R.string.text_error_password_empty)
            false
        } else if (password.length < 8) {
            binding.etlPassword.isErrorEnabled = true
            binding.etlPassword.error = getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            binding.etlPassword.isErrorEnabled = false
            true
        }
    }
}