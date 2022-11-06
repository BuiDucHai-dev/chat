package com.example.stalk.invite_friend

import android.content.Context
import com.example.stalk.invite_friend.model.PhoneContact

class InviteFriendRepository(private val dataSource: InviteFriendDataSource) {
    fun getContactList(context: Context, callback: Callback) {
        dataSource.getContactList(context, object: InviteFriendDataSource.Callback{
            override fun onReceiveContactList(list: ArrayList<PhoneContact>) {
                callback.onReceiveContactList(list)
            }
        })
    }

    interface Callback{
        fun onReceiveContactList(list: ArrayList<PhoneContact>)
    }
}