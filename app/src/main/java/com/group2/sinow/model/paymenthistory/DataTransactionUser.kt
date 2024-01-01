package com.group2.sinow.model.paymenthistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTransactionUser(
    val transactions: List<TransactionUser>?
) : Parcelable
