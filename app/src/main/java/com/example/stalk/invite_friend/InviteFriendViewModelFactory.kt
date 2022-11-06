package com.example.stalk.invite_friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InviteFriendViewModelFactory : ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InviteFriendViewModel::class.java)) {
            return InviteFriendViewModel(
                repository = InviteFriendRepository(
                    dataSource = InviteFriendDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}