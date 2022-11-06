package com.example.stalk.login.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VerificationViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VerificationViewModel::class.java)) {
            return VerificationViewModel(
                repository = VerificationRepository(
                    dataSource = VerificationDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}