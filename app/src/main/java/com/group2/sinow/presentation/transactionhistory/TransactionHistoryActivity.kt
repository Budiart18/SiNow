package com.group2.sinow.presentation.transactionhistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityPaymentHistoryBinding
import com.group2.sinow.presentation.transactionhistory.detailtransactionhistory.DetailTransactionHistoryActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionHistoryActivity : AppCompatActivity() {

    private val binding: ActivityPaymentHistoryBinding by lazy {
        ActivityPaymentHistoryBinding.inflate(layoutInflater)
    }

    private val viewModel: TransactionHistoryViewModel by viewModel()

    private val transactionAdapter: PaymentHistoryAdapter by lazy {
        PaymentHistoryAdapter { transactionUser ->
            transactionUser.id?.let { id -> navigateToDetailPayment(id) }
        }
    }

    private fun navigateToDetailPayment(transactionId: String) {
        DetailTransactionHistoryActivity.startActivity(this, transactionId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeTransactionList()
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
        viewModel.getUserTransaction()
    }

    private fun observeTransactionList() {
        viewModel.transaction.observe(this) {
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
                    binding.layoutTransactionEmpty.root.isVisible = false
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = true
                    binding.layoutStateClassTopic.tvError.isVisible = false
                    binding.rvListPaymentHistory.isVisible = false
                },
                doOnError = {
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = false
                    binding.rvListPaymentHistory.isVisible = false
                    if (it.exception is ApiException) {
                        if (it.exception.httpCode == 401) {
                            binding.layoutTransactionEmpty.root.isVisible = true
                        } else {
                            binding.layoutTransactionEmpty.root.isVisible = true
                            binding.layoutTransactionEmpty.tvNotificationEmpty.text = getString(R.string.tv_you_dont_have_any_transaction)
                        }
                    }
                }
            )
        }
    }
}
