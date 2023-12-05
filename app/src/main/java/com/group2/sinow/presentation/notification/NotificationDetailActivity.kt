package com.group2.sinow.presentation.notification

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.group2.sinow.databinding.ActivityNotificationDetailBinding
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotificationDetailActivity : AppCompatActivity() {

    private val binding: ActivityNotificationDetailBinding by lazy {
        ActivityNotificationDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: NotificationDetailViewModel by viewModel{
        parametersOf(intent.extras ?: bundleOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindNotification(viewModel.notification)
        observeData()
        getData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {
        viewModel.getNotification()
    }

    private fun observeData() {
        viewModel.notificationData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen (
                doOnSuccess = {
                    binding.layoutStateNotification.root.isVisible = false
                    binding.layoutStateNotification.loadingAnimation.isVisible = false
                    binding.layoutStateNotification.tvError.isVisible = false
                },
                doOnLoading = {
                    binding.layoutStateNotification.root.isVisible = true
                    binding.layoutStateNotification.loadingAnimation.isVisible = true
                    binding.layoutStateNotification.tvError.isVisible = false
                },
                doOnError = {
                    binding.layoutStateNotification.root.isVisible = true
                    binding.layoutStateNotification.loadingAnimation.isVisible = false
                    binding.layoutStateNotification.tvError.isVisible = true
                    binding.layoutStateNotification.tvError.text = it.exception?.message.orEmpty()
                }
            )
        }
    }

    private fun bindNotification(notification: Notification?) {
        notification?.let { item ->
            binding.tvNotificationCategory.text = item.type
            binding.tvNotificationTitle.text = item.title
            binding.tvNotificationDesc.text = item.content
            binding.tvNotificationTime.text = item.createdAt
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"

        fun startActivity(context: Context, notification: Notification) {
            val intent = Intent(context, NotificationDetailActivity::class.java)
            intent.putExtra(EXTRA_NOTIFICATION, notification)
            context.startActivity(intent)
        }
    }

}