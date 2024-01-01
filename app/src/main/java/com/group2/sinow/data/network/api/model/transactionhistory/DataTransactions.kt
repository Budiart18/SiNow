package com.group2.sinow.data.network.api.model.transactionhistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DataTransactions(
    @SerializedName("transactions")
    val transactions: List<Transaction>?
)
