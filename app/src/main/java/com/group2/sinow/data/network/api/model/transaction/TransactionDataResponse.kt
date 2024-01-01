package com.group2.sinow.data.network.api.model.transaction

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.group2.sinow.model.transaction.TransactionData

@Keep
data class TransactionDataResponse(
    @SerializedName("token")
    val token: String?,
    @SerializedName("redirect_url")
    val redirectUrl: String?,
    @SerializedName("transactionDetail")
    val transactionDetail: TransactionDetailResponse?
)

fun TransactionDataResponse.toTransactionData() = TransactionData(
    token = this.token.orEmpty(),
    redirectUrl = this.redirectUrl.orEmpty(),
    transactionDetail = this.transactionDetail?.toTransactionDetail()
)
