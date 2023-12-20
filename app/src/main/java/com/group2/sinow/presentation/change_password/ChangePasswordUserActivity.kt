package com.group2.sinow.presentation.change_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputLayout
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityChangePasswordUserBinding
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.shashank.sony.fancytoastlib.FancyToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordUserActivity : AppCompatActivity() {

    private val binding: ActivityChangePasswordUserBinding by lazy {
        ActivityChangePasswordUserBinding.inflate(layoutInflater)
    }

    private val viewModel: ChangePasswordUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeResult()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun observeResult() {
        viewModel.changePasswordResult.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.etEnterOldPassword.text?.clear()
                    binding.etEnterNewPassword.text?.clear()
                    binding.etRepeatNewPassword.text?.clear()
                    binding.pbLoading.isVisible = false
                    binding.btnChangePassword.isVisible = true
                    FancyToast.makeText(
                        this,
                        getString(R.string.text_password_successfully_changed),
                        FancyToast.LENGTH_LONG,
                        FancyToast.SUCCESS,
                        false
                    ).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangePassword.isVisible = false
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangePassword.isVisible = true
                    if (it.exception is ApiException) {
                        FancyToast.makeText(
                            this,
                            it.exception.getParsedError()?.message.orEmpty(),
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                }
            )
        }
    }

    private fun isFormValid(): Boolean {
        val oldPassword = binding.etEnterOldPassword.text.toString().trim()
        val newPassword = binding.etEnterNewPassword.text.toString().trim()
        val confirmNewPassword = binding.etRepeatNewPassword.text.toString().trim()
        return checkPasswordValidation(
            oldPassword,
            binding.tilEnterOldPassword
        ) && checkPasswordValidation(
            newPassword,
            binding.tilEnterNewPassword
        ) && checkPasswordValidation(confirmNewPassword, binding.tilRepeatNewPassword)
    }

    private fun checkPasswordValidation(password: String, tilPassword: TextInputLayout): Boolean {
        return if (password.isEmpty()) {
            tilPassword.isErrorEnabled = true
            tilPassword.error = getString(R.string.text_error_password_empty)
            false
        } else if (password.length < 8) {
            tilPassword.isErrorEnabled = true
            tilPassword.error = getString(R.string.text_error_password_less_than_8_char)
            false
        } else {
            tilPassword.isErrorEnabled = false
            true
        }
    }

    private fun changePassword() {
        if (isFormValid()) {
            val oldPassword = binding.etEnterOldPassword.text.toString().trim()
            val newPassword = binding.etEnterNewPassword.text.toString().trim()
            val confirmNewPassword = binding.etRepeatNewPassword.text.toString().trim()
            viewModel.changePassword(oldPassword, newPassword, confirmNewPassword)
        }
    }
}