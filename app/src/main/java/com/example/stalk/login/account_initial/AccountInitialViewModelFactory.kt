package com.example.stalk.login.account_initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AccountInitialViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountInitialViewModel::class.java)) {
            return AccountInitialViewModel(
                repository = AccountInitialRepository(
                    dataSource = AccountInitialDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}