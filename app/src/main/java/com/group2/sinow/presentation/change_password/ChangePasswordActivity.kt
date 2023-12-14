package com.group2.sinow.presentation.change_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListener()
    }

    private fun setupListener() {
        binding.btnChangePassword.setOnClickListener {
            if(validateData()) {
                changePassword()
            }
        }

        binding.idBackButton.setOnClickListener {
            onNavigateUp()
        }
    }

    private fun validateData() : Boolean {
        var error = 0

        if (binding.etEnterOldPassword.text.toString().isEmpty()) {
            error++
            binding.etEnterOldPassword.error = "Please enter password!"
        }

        if(binding.etEnterNewPassword.text.toString().isEmpty()) {
            error++
            binding.etEnterNewPassword.error = "Please enter password!"
        }

        if(binding.etRepeatNewPassword.text.toString().isEmpty()) {
            error++
            binding.etRepeatNewPassword.error = "Please enter password!"
        }

        if(binding.etEnterNewPassword.text.toString() != binding.etRepeatNewPassword.text.toString()) {
            error++
            binding.etRepeatNewPassword.error = "Password is not same!"
        }

        return error == 0
    }

    private fun changePassword() {

    }
}