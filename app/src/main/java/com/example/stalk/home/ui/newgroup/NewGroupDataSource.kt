package com.example.stalk.home.ui.newgroup

import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User

class NewGroupDataSource {
    fun getUserList(callback: NewGroupDataSourceCallback) {
        MyFirebase().db.collection("users")
            .orderBy("fullName")
            .whereArrayContains("friend", MyFirebase().auth.currentUser!!.uid)
            .get()
            .addOnCompleteListener {
                callback.onReceiverUserList(it.result.toObjects(User::class.java))
            }
    }

    interface NewGroupDataSourceCallback{
        fun onReceiverUserList(listUser: MutableList<User>)
    }
}