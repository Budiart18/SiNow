package com.group2.sinow.presentation.transactionhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.group2.sinow.databinding.ActivityPaymentHistoryBinding
import com.group2.sinow.model.paymenthistory.TransactionUser
import com.group2.sinow.presentation.notification.notificationdetail.NotificationDetailActivity
import com.group2.sinow.presentation.transactionhistory.detailtransactionhistory.DetailTransactionHistoryActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionHistoryActivity : AppCompatActivity() {

    private val binding : ActivityPaymentHistoryBinding by lazy {
        ActivityPaymentHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel : TransactionHistoryViewModel by viewModel()

    private val transactionAdapter : PaymentHistoryAdapter by lazy {
        PaymentHistoryAdapter{
            navigateToDetailPayment(it)
        }

    }

    private fun navigateToDetailPayment(transaction: TransactionUser) {
        DetailTransactionHistoryActivity.startActivity(this, transaction)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
        observeTransactionList()
        setClickListener()
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
        viewModel.getUserTransaction()
    }

    private fun observeTransactionList() {
        viewModel.transaction.observe(this){
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateClassTopic.root.isVisible = false
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = false
                    binding.layoutStateClassTopic.tvError.isVisible = false
                    binding.rvListPaymentHistory.apply {
                        isVisible = true
                        adapter = transactionAdapter
                    }
                    it.payload?.let { data -> transactionAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = true
                    binding.layoutStateClassTopic.tvError.isVisible = false
                    binding.rvListPaymentHistory.isVisible = false
                },
                doOnError = {
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = false
                    binding.rvListPaymentHistory.isVisible = false
                    if(it.exception is ApiException){
                        if(it.exception.httpCode == 401){
                            binding.layoutCourseEmpty.root.isVisible = true
                        }else{
                            binding.layoutCourseEmpty.root.isVisible = true
                        }
                    }
                }
            )
        }
    }
}