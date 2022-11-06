package com.example.stalk.home.ui.find_friend

import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User

class FindFriendDataSource {
    fun findFriend(phoneNum: String, callback: Callback) {
        MyFirebase().db.collection("users")
            .whereEqualTo("phoneNum", phoneNum)
            .get()
            .addOnSuccessListener {
                var user: User? = null
                if (!it.isEmpty){
                    user = it.documents[0].toObject(User::class.java)
                }
                callback.onReceiverUser(user)
            }
    }

    fun requestFriend(user: User) {
        updateCurrentUser(user)
        updateFriendUser(user)
    }

    private fun updateFriendUser(user: User) {
        MyFirebase().db.collection("users")
            .document(user.userId.toString())
            .get()
            .addOnSuccessListener {
                val u : User = it.toObject(User::class.java)!!
                u.friend!!.add(MyFirebase().auth.currentUser?.uid.toString())

                MyFirebase().db.collection("users")
                    .document(user.userId.toString())
                    .update("friend", u.friend)
            }
    }

    private fun updateCurrentUser(user: User) {
        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {
                val u : User = it.toObject(User::class.java)!!
                u.friend!!.add(user.userId.toString())

                MyFirebase().db.collection("users")
                    .document(MyFirebase().auth.currentUser?.uid.toString())
                    .update("friend", u.friend)
            }
    }

    interface Callback{
        fun onReceiverUser(user: User?)
    }
}