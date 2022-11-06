package com.example.stalk.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessViewModelFactory(var conId: String): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessViewModel::class.java)){
            return MessViewModel(
                repository = MessRepository(
                    dataSource = MessDataSource()
                ), conId = conId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}