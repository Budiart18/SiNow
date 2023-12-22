package com.group2.sinow.model.transaction

import com.group2.sinow.data.network.api.model.transaction.TransactionDetailResponse

data class TransactionData(
    val token: String?,
    val redirectUrl: String?,
    val transactionDetail: TransactionDetail?
)
