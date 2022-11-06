package com.example.stalk.home.ui.find_friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.User

class FindFriendViewModel(private val repository: FindFriendRepository) : ViewModel() {

    private val _reUser = MutableLiveData<User?>()
    val reUser: LiveData<User?> = _reUser

    fun findFriend(phoneNum: String) {
        repository.findFriend(phoneNum, object : FindFriendRepository.Callback{
            override fun onReceiverUser(user: User?) {
                _reUser.value = user
            }
        })
    }

    fun requestFriend(user: User) {
        repository.requestFriend(user)
    }
}