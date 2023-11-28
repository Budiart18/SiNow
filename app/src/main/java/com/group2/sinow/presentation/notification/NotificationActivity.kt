package com.group2.sinow.presentation.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}