package com.group2.sinow.data.network.api.model.transactionhistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class DataDetailTransaction(
    @SerializedName("transaction")
    val transaction: Transaction
)
