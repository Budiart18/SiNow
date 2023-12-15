package com.group2.sinow.presentation.change_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.group2.sinow.databinding.ActivityChangePasswordUserBinding
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordUserActivity : AppCompatActivity() {

    private val binding: ActivityChangePasswordUserBinding by lazy {
        ActivityChangePasswordUserBinding.inflate(layoutInflater)
    }

    private val viewModel : ChangePasswordUserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeResult()
        setClickListener()
    }

    private fun setClickListener() {
        binding.idBackButton.setOnClickListener {
            onBackPressed()
        }
        binding.btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun observeResult() {
        viewModel.changePasswordResult.observe(this){ resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.etEnterOldPassword.text?.clear()
                    binding.etEnterNewPassword.text?.clear()
                    binding.etRepeatNewPassword.text?.clear()
                    binding.pbLoading.isVisible = false
                    binding.btnChangePassword.isVisible = true
                    Toast.makeText(this, "Password Berhasil diubah", Toast.LENGTH_SHORT).show()
                },
                doOnLoading = {
                    binding.pbLoading.isVisible = true
                    binding.btnChangePassword.isVisible = false
                },
                doOnError = {
                    binding.pbLoading.isVisible = false
                    binding.btnChangePassword.isVisible = true
                    if (it.exception is ApiException) {
                        Toast.makeText(this, it.exception.getParsedError()?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    private fun changePassword() {
        val oldPassword = binding.etEnterOldPassword.text.toString().trim()
        val newPassword = binding.etEnterNewPassword.text.toString().trim()
        val confirmNewPassword = binding.etRepeatNewPassword.text.toString().trim()
        viewModel.changePassword(oldPassword, newPassword, confirmNewPassword)
    }
}