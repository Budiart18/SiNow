package com.group2.sinow.data.network.api.model.transactionhistory


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class DataDetailTransaction(
    @SerializedName("transaction")
    val transaction: Transaction
)
