package com.group2.sinow.data.network.api.model.transactionhistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TransactionDetailResponse(
    @SerializedName("status")
    val status: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: DataDetailTransaction
)
