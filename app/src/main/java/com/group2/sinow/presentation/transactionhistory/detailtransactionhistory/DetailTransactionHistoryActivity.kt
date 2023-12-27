package com.group2.sinow.presentation.transactionhistory.detailtransactionhistory

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailPaymentHistoryBinding
import com.group2.sinow.model.paymenthistory.TransactionUser
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.payment.PaymentActivity
import com.group2.sinow.presentation.transactionhistory.TransactionHistoryActivity
import com.group2.sinow.utils.changeFormatTime
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.group2.sinow.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailTransactionHistoryActivity : AppCompatActivity() {

    private val binding: ActivityDetailPaymentHistoryBinding by lazy {
        ActivityDetailPaymentHistoryBinding.inflate(layoutInflater)

    }

    private val viewModel: DetailTransactionHistoryViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
        setClickListener()
        observeDeleteTransactionResult()
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
        val transactionId = intent.getStringExtra(EXTRA_TRANSACTION)
        if (transactionId != null) {
            viewModel.getTransaction(transactionId)
        }
    }

    private fun observeData() {
        viewModel.transactionData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateTransaction.root.isVisible = false
                    binding.layoutStateTransaction.loadingAnimation.isVisible = false
                    binding.layoutStateTransaction.tvError.isVisible = false
                    bindHistoryTransaction(it.payload)
                },
                doOnLoading = {
                    binding.layoutStateTransaction.root.isVisible = true
                    binding.layoutStateTransaction.loadingAnimation.isVisible = true
                    binding.layoutStateTransaction.tvError.isVisible = false
                },
                doOnError = {
                    binding.layoutStateTransaction.root.isVisible = true
                    binding.layoutStateTransaction.loadingAnimation.isVisible = false
                    binding.layoutStateTransaction.tvError.isVisible = true
                    binding.layoutStateTransaction.tvError.text = it.exception?.message.orEmpty()
                }
            )
        }
    }

    private fun observeDeleteTransactionResult() {
        viewModel.deleteTransactionResult.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    navigateToTransactionList()
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

    private fun navigateToTransactionList() {
        val intent = Intent(this, TransactionHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun deleteTransaction(id: String) {
        val dialog = AlertDialog.Builder(this)
            .setMessage(getString(R.string.text_cancel_payment_confirmation))
            .setPositiveButton(
                getString(R.string.text_yes)
            ) { _, _ ->
                viewModel.deleteTransaction(id)
            }
            .setNegativeButton(
                getString(R.string.text_no)
            ) { _, _ ->
                // no-op , do nothing
            }.create()
        dialog.show()
    }


    private fun bindHistoryTransaction(transaction: TransactionUser?) {
        transaction?.let {
            binding.itemPayment.itemCost.tvItemTitle.text = it.courseUser?.name
            binding.itemPayment.itemCost.tvItemPrice.text =
                it.coursePrice?.toDouble()?.toCurrencyFormat()
            if (it.promoDiscountPercentage != 0) {
                binding.itemPayment.itemCostDiscount.root.isVisible = true
                binding.itemPayment.itemCostDiscount.tvItemTitle.text =
                    getString(R.string.tv_discount_course, it.promoDiscountPercentage)
            } else {
                binding.itemPayment.itemCostDiscount.root.isVisible = false
            }
            binding.itemPayment.itemCostDiscount.tvItemPrice.text = getString(
                R.string.discounted_price,
                it.discountPrice?.toDouble()?.toCurrencyFormat()
            )
            binding.itemPayment.tvItemTax.text = getString(R.string.format_tax, it.taxPercentage)
            binding.itemPayment.tvItemTotalPrice.text =
                it.totalPrice?.toDouble()?.toCurrencyFormat()

            if (!it.paymentMethod.isNullOrEmpty()) {
                binding.itemInformation.clPaymentMethod.visibility = View.VISIBLE

                binding.itemInformation.tvResultPaymentMethod.text = when (it.paymentMethod) {
                    CREDIT_CARD -> getString(R.string.credit_card)
                    GOPAY -> getString(R.string.gopay)
                    QRIS -> getString(R.string.qris)
                    SHOPEEPAY -> getString(R.string.shopeepay)
                    BANK_TRANSFER -> getString(R.string.bank_transfer)
                    ECHANNEL -> getString(R.string.e_channel)
                    CSTORE -> getString(R.string.pay_at_store)

                    else -> it.paymentMethod
                }
            } else {
                binding.itemInformation.clPaymentMethod.visibility = View.GONE
            }

            when (it.status) {
                BELUM_BAYAR -> {
                    binding.itemInformation.tvResultPaymentStatus.text =
                        getString(R.string.waiting_for_payment)
                    binding.btnNavigate.text = getString(R.string.pay_now)
                    binding.btnNavigate.isVisible = true
                    binding.btnCancel.isVisible = true
                    binding.btnCancel.setOnClickListener { _ ->
                        it.id?.let { it1 -> deleteTransaction(it1) }
                    }
                    binding.btnNavigate.setOnClickListener { _ ->
                        navigateToPayment(it.paymentUrl)
                    }
                }

                SUDAH_BAYAR -> {
                    binding.itemInformation.tvResultPaymentStatus.text = getString(R.string.finish)
                    binding.btnNavigate.isVisible = true
                    binding.btnCancel.isVisible = false
                    binding.btnNavigate.text = getString(R.string.open_course)
                    binding.btnNavigate.setOnClickListener { _ ->
                        navigateToDetail(courseId = it.courseId)
                    }
                }

                else -> {
                    binding.itemInformation.tvResultPaymentStatus.text = getString(R.string.failed)
                    binding.btnNavigate.isVisible = false
                    binding.btnCancel.isVisible = false
                }
            }

            binding.itemInformation.tvResultNumberOrder.text = it.noOrder
            binding.itemInformation.tvResultOrderTime.text =
                changeFormatTime(it.createdAt.toString())

            if (!it.paidAt.isNullOrEmpty()) {
                binding.itemInformation.clPaymentTime.visibility = View.VISIBLE
                binding.itemInformation.tvResultPaymentTime.text =
                    changeFormatTime(it.paidAt.toString())
            } else {
                binding.itemInformation.clPaymentTime.visibility = View.GONE
            }

            if (!it.paidAt.isNullOrEmpty()) {
                binding.itemInformation.clExpiredTime.visibility = View.GONE
            } else {
                binding.itemInformation.clExpiredTime.visibility = View.VISIBLE
                binding.itemInformation.tvResultExpiredTime.text =
                    changeFormatTime(it.expiredAt.toString())
            }

        }

    }

    private fun navigateToDetail(courseId: Int?) {
        DetailCourseActivity.startActivity(this, courseId)
    }

    private fun navigateToPayment(redirectUrl: String?) {
        val intent = Intent(this, PaymentActivity::class.java).apply {
            putExtra(URL, redirectUrl)
        }
        startActivity(intent)

    }

    companion object {
        const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"
        const val BELUM_BAYAR = "BELUM_BAYAR"
        const val SUDAH_BAYAR = "SUDAH_BAYAR"
        const val CREDIT_CARD = "credit_card"
        const val GOPAY = "gopay"
        const val QRIS = "qris"
        const val SHOPEEPAY = "shopeepay"
        const val BANK_TRANSFER = "bank_transfer"
        const val ECHANNEL = "echannel"
        const val CSTORE = "cstore"
        const val URL = "URL"

        fun startActivity(context: Context, transactionId: String) {
            val intent = Intent(context, DetailTransactionHistoryActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION, transactionId)
            context.startActivity(intent)
        }
    }


}



