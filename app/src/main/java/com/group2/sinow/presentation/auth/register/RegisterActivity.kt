package com.group2.sinow.presentation.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}