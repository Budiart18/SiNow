package com.group2.sinow.presentation.transactionhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group2.sinow.data.repository.UserRepository
import com.group2.sinow.model.paymenthistory.TransactionUser
import com.group2.sinow.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionHistoryViewModel(private val repository: UserRepository
) : ViewModel() {

    private val _transaction = MutableLiveData<ResultWrapper<List<TransactionUser>>>()
    val transaction: LiveData<ResultWrapper<List<TransactionUser>>>
        get() = _transaction

    fun getUserTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserTransactionHistory().collect {
                _transaction.postValue(it)
            }
        }
    }
}