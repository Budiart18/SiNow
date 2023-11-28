package com.group2.sinow.presentation.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityNotificationDetailBinding

class NotificationDetailActivity : AppCompatActivity() {

    private val binding: ActivityNotificationDetailBinding by lazy {
        ActivityNotificationDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}