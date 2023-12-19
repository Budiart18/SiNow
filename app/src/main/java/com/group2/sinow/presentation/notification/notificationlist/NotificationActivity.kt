package com.group2.sinow.presentation.notification.notificationlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityNotificationBinding
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.presentation.notification.notificationdetail.NotificationDetailActivity
import com.group2.sinow.presentation.notification.adapter.NotificationAdapter
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : AppCompatActivity() {

    private val binding: ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    private val notificationAdapter: NotificationAdapter by lazy {
        NotificationAdapter {
            navigateToNotificationDetail(it)
        }
    }

    private fun navigateToNotificationDetail(notification: Notification) {
        NotificationDetailActivity.startActivity(this, notification)
    }

    private val viewModel: NotificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeNotificationData()
        setClickListener()
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.swipeRefresh.setOnRefreshListener {
            getData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun getData() {
        viewModel.getNotifications()
    }

    private fun observeNotificationData() {
        viewModel.notifications.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateNotification.root.isVisible = false
                    binding.layoutStateNotification.loadingAnimation.isVisible = false
                    binding.layoutStateNotification.tvError.isVisible = false
                    binding.layoutNotificationEmpty.root.isVisible = false
                    binding.rvNotificationList.apply {
                        isVisible = true
                        adapter = notificationAdapter
                    }
                    it.payload?.let { data -> notificationAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStateNotification.root.isVisible = true
                    binding.layoutStateNotification.loadingAnimation.isVisible = true
                    binding.layoutStateNotification.tvError.isVisible = false
                    binding.layoutNotificationEmpty.root.isVisible = false
                    binding.rvNotificationList.isVisible = false
                },
                doOnError = {
                    binding.layoutStateNotification.root.isVisible = true
                    binding.layoutStateNotification.loadingAnimation.isVisible = false
                    binding.rvNotificationList.isVisible = false
                    if (it.exception is ApiException) {
                        if (it.exception.httpCode == 401) {
                            binding.layoutNotificationEmpty.root.isVisible = true
                            binding.layoutNotificationEmpty.tvNotificationEmpty.text =
                                getString(R.string.tv_user_must_login_first)
                        } else {
                            binding.layoutNotificationEmpty.root.isVisible = true
                        }
                    }
                }
            )
        }
    }

}