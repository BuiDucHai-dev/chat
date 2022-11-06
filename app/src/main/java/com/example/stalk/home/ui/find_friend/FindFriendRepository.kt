package com.example.stalk.home.ui.find_friend

import com.example.stalk.common.model.User

class FindFriendRepository(val dataSource: FindFriendDataSource) {
    fun findFriend(phoneNum: String, callback: Callback) {
        dataSource.findFriend(phoneNum, object: FindFriendDataSource.Callback{
            override fun onReceiverUser(user: User?) {
                callback.onReceiverUser(user)
            }
        })
    }

    fun requestFriend(user: User) {
        dataSource.requestFriend(user)
    }

    interface Callback{
        fun onReceiverUser(user: User?)
    }
}