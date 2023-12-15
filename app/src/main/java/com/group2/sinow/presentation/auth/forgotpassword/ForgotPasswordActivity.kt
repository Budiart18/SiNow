package com.group2.sinow.presentation.auth.forgotpassword

import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.group2.sinow.databinding.ActivityForgotPasswordBinding
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen

class ForgotPasswordActivity : AppCompatActivity() {

    private val binding: ActivityForgotPasswordBinding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }

    private val viewModel: ForgotPasswordViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
        observeResult()
    }

    private fun observeResult() {
        viewModel.resetStatus.observe(this){result ->
            result.proceedWhen (
                doOnSuccess = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = false
                    binding.btnChangePassword.isVisible  = true
                    binding.tvSuccessMessage.isVisible = true
                },
                doOnLoading = {
                    binding.btnChangePassword.isVisible = true
                    binding.progressBar.isVisible = true
                    binding.tvErrorMessage.isVisible = false

                },
                doOnError = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = true
                    if (it.exception is ApiException){
                        binding.tvErrorMessage.text = it.exception.getParsedError()?.message
                    }
                }


            )
        }
    }

    private fun setOnClickListener() {
        binding.btnChangePassword.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            viewModel.resetPassword(email)
        }
    }


}