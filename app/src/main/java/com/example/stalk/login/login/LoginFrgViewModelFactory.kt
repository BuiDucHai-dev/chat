package com.example.stalk.login.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginFrgViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginFrgViewModel::class.java)) {
            return LoginFrgViewModel(
                repository = LoginFrgRepository(
                    dataSource = LoginFrgDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}