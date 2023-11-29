package com.group2.sinow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.databinding.ActivityMainBinding
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.presentation.bottom_dialog.PaymentSuccessDialogFragment
import com.group2.sinow.presentation.bottom_dialog.RegistrationSuccessDialogFragment
import com.group2.sinow.presentation.bottom_dialog.StartLearningDialogFragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListener()
    }

    private fun setClickListener() {
        binding.test1.setOnClickListener {
            StartLearningDialogFragment().show(supportFragmentManager, null)
        }
        binding.test2.setOnClickListener {
            NonLoginUserDialogFragment().show(supportFragmentManager, null)
        }
        binding.test3.setOnClickListener {
            PaymentSuccessDialogFragment().show(supportFragmentManager, null)
        }
        binding.test4.setOnClickListener {
            RegistrationSuccessDialogFragment().show(supportFragmentManager, null)
        }
    }

}