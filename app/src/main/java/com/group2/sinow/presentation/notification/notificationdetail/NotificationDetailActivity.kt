package com.group2.sinow.presentation.notification.notificationdetail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityNotificationDetailBinding
import com.group2.sinow.model.notification.Notification
import com.group2.sinow.presentation.notification.notificationlist.NotificationActivity
import com.group2.sinow.utils.changeFormatTime
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NotificationDetailActivity : AppCompatActivity() {

    private val binding: ActivityNotificationDetailBinding by lazy {
        ActivityNotificationDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: NotificationDetailViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindNotification(viewModel.notification)
        observeData()
        getData()
        setClickListener()
        observeDeleteNotificationResult()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.ivDelete.setOnClickListener {
            deleteNotification()
        }
    }

    private fun deleteNotification() {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.text_delete_notification_confirmation))
            .setPositiveButton(
                getString(R.string.text_yes)
            ) { _, _ ->
                viewModel.deleteNotification()
            }
            .setNegativeButton(
                getString(R.string.text_no)
            ) { _, _ ->
                // no-op , do nothing
            }.create()
        dialog.show()
    }

    private fun navigateToNotificationList() {
        val intent = Intent(this, NotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun getData() {
        viewModel.getNotification()
    }

    private fun observeData() {
        viewModel.notificationData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
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
                    if (it.exception is ApiException) {
                        binding.layoutStateNotification.tvError.text =
                            it.exception.getParsedError()?.message.orEmpty()
                    }
                }
            )
        }
    }

    private fun observeDeleteNotificationResult() {
        viewModel.deleteNotificationResult.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    navigateToNotificationList()
                },
                doOnError = { err ->
                    if (err.exception is ApiException) {
                        Toast.makeText(
                            this,
                            err.exception.getParsedError()?.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun bindNotification(notification: Notification?) {
        notification?.let { item ->
            binding.tvNotificationCategory.text = item.type
            binding.tvNotificationTitle.text = item.title
            binding.tvNotificationDesc.text = item.content
            binding.tvNotificationTime.text = changeFormatTime(item.createdAt.toString())
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