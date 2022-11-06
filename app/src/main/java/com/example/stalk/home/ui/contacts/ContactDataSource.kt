package com.example.stalk.home.ui.contacts

import android.util.Log
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.Conversation
import com.example.stalk.common.model.User
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class ContactDataSource {

    companion object{
        const val CON_ID = "CON_ID"
    }

    fun getContactList(callback: ContactDataSourceCallback){
        MyFirebase().db.collection("users")
            .orderBy("dtmLastSeen", Query.Direction.DESCENDING)
            .whereArrayContains("friend", MyFirebase().auth.currentUser!!.uid)
            .addSnapshotListener{snapshots, e ->
                if (e != null) {
                    Log.d("hai.bd", e.message.toString())
                }
                if (snapshots != null){
                    callback.onReceiveContactList(snapshots.toObjects(), false)
                } else {
                    Log.d("hai.bd", "List contact is null or empty")
                }
            }
    }

    fun getContactListByName(callback: ContactDataSourceCallback){
        MyFirebase().db.collection("users")
            .orderBy("fullName")
            .whereArrayContains("friend", MyFirebase().auth.currentUser!!.uid)
            .addSnapshotListener{snapshots, e ->
                if (e != null) {
                    Log.d("hai.bd", e.message.toString())
                }
                if (snapshots != null){
                    callback.onReceiveContactList(snapshots.toObjects(), true)
                } else {
                    Log.d("hai.bd", "List contact is null or empty")
                }
            }
    }

    fun getConversationId(userId: String, callback: ContactDataSourceConversationCallback) {
        MyFirebase().db.collection("conversation")
            .whereArrayContains("member", MyFirebase().auth.currentUser?.uid.toString())
            .whereEqualTo("group", false)
            .get()
            .addOnCompleteListener {
                val list: MutableList<Conversation> = it.result.toObjects(Conversation::class.java)
                var conId = CON_ID
                list.forEach { conv ->
                    if (conv.member.contains(userId)){
                        conId = conv.conId.toString()
                    }
                }
                callback.onReceiverConversationId(conId)
            }
    }

    fun createNewConversation(selectedUser: User, currentUser: User, callback: ContactDataSourceCreateConversationCallback) {
        val conversation = Conversation(
            avatar = listOf(selectedUser.avatar, currentUser.avatar),
            conId = "newConversation",
            dtmModify = Comm().getCurrentTime(),
            group = false,
            lastMsg = "(no message)",
            member = listOf(selectedUser.userId.toString(), currentUser.userId.toString()),
            title = listOf(selectedUser.fullName, currentUser.fullName),
            read = emptyList()
        )
        MyFirebase().db.collection("conversation")
            .add(conversation)
            .addOnSuccessListener {
                MyFirebase().colRef
                    .whereEqualTo("conId", "newConversation")
                    .get()
                    .addOnSuccessListener { id ->
                        if (!id.isEmpty){
                            setConversationId(id.first().id)
                        }
                        callback.onCreateConversationSuccess(id.first().id)
                    }
            }
            .addOnFailureListener {
                callback.onCreateConversationFail()
            }
    }

    private fun setConversationId(conId: String) {
        MyFirebase().db.collection("conversation")
            .document(conId)
            .update("conId", conId)
    }

    interface ContactDataSourceCallback{
        fun onReceiveContactList(listContact: List<User>, isByName: Boolean)
    }

    interface ContactDataSourceConversationCallback{
        fun onReceiverConversationId(conId: String)
    }

    interface ContactDataSourceCreateConversationCallback{
        fun onCreateConversationSuccess(conId: String)
        fun onCreateConversationFail()
    }
}
