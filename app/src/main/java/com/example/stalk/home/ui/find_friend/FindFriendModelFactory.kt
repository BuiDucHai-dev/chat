package com.example.stalk.home.ui.find_friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FindFriendModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindFriendViewModel::class.java)){
            return FindFriendViewModel(
                repository = FindFriendRepository(
                    dataSource = FindFriendDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}