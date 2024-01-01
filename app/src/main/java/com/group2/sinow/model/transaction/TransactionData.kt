package com.group2.sinow.model.transaction

data class TransactionData(
    val token: String?,
    val redirectUrl: String?,
    val transactionDetail: TransactionDetail?
)
