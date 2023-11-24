package com.group2.sinow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.databinding.ActivityMainBinding
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}