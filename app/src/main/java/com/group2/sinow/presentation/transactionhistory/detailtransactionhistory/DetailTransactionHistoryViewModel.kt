package com.group2.sinow.presentation.transactionhistory.detailtransactionhistory

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.paymenthistory.DataDetailTransactionUser
import com.group2.sinow.model.paymenthistory.TransactionUser
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailTransactionHistoryViewModel(
    private val extra: Bundle?,
    private val repository: UserRepository
) : ViewModel() {

    val transaction = extra?.getParcelable<TransactionUser>(DetailTransactionHistoryActivity.EXTRA_TRANSACTION)

    private val _transaction = MutableLiveData<ResultWrapper<TransactionUser>>()
    val transactionData: LiveData<ResultWrapper<TransactionUser>>
        get() = _transaction

    fun getTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserDetailTransaction(transaction?.id.orEmpty()).collect{
                _transaction.postValue(it)
            }
        }
    }
}