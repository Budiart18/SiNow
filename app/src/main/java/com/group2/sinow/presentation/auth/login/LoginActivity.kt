package com.group2.sinow.presentation.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}