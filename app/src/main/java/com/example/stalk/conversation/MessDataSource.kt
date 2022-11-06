package com.example.stalk.conversation

import android.util.Log
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.Conversation
import com.example.stalk.conversation.model.Message
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects

class MessDataSource {

    fun getListMessage(conId: String, callback: MessDataSourceCallback){
        MyFirebase().db.collection("conversation")
            .document(conId)
            .collection("messages")
            .orderBy("dtmRegister", Query.Direction.DESCENDING)
            .addSnapshotListener{ snapshots, _ ->
                if (snapshots != null){
                    callback.onReceiveMessageList(snapshots.toObjects())
                } else {
                    Log.d("hai.bd", "List conversation is null or empty")
                }
            }
    }


    fun sendMess(conversationId: String, content: String){
        var currentTime = Comm().getCurrentTime()
        val mess = hashMapOf(
            "content" to content,
            "dtmRegister" to currentTime,
            "msgId" to "defaultMessId",
            "owner" to MyFirebase().auth.currentUser?.uid,
            "state" to ""
        )

        MyFirebase().db.collection("conversation")
            .document(conversationId)
            .collection("messages")
            .add(mess)
            .addOnSuccessListener {
                getMessage(conversationId)
            }

        val updates = hashMapOf(
            "lastMsg" to content,
            "read" to listOf(MyFirebase().auth.currentUser?.uid),
            "dtmModify" to currentTime
        )
        MyFirebase().db.collection("conversation")
            .document(conversationId)
            .update(updates)
    }

    private fun getMessage(conversationId: String){
        MyFirebase().db.collection("conversation")
            .document(conversationId)
            .collection("messages")
            .whereEqualTo("msgId", "defaultMessId")
            .get()
            .addOnSuccessListener {
                updateMessId(it.documents[0].id, conversationId)
            }
    }

    private fun updateMessId(msgId: String, conversationId: String) {
        MyFirebase().db.collection("conversation")
            .document(conversationId)
            .collection("messages")
            .document(msgId)
            .update("msgId", msgId)
    }

    fun deleteMessage(conId: String, selectedList: ArrayList<Message>, isSelectAll: Boolean) {
        for (message in selectedList){
            MyFirebase().db.collection("conversation")
                .document(conId)
                .collection("messages")
                .document(message.msgId.toString())
                .delete()
        }
        if (isSelectAll){
            MyFirebase().db.collection("conversation")
                .document(conId)
                .update("lastMsg", "(no message)")
        }
    }

    fun getConversation(conId: String?, callback: MessDataSourceCallback) {
        MyFirebase().db.collection("conversation")
            .document(conId!!)
            .get()
            .addOnSuccessListener {
                if (it.exists()){
                    callback.onReceiveConversation(it.toObject(Conversation::class.java)!!)
                }
            }
    }

    fun clearHistory(conId: String) {
        MyFirebase().db.collection("conversation")
            .document(conId)
            .collection("messages")
            .get()
            .addOnSuccessListener {
                it.forEach { it2 ->
                    MyFirebase().db.collection("conversation")
                        .document(conId)
                        .collection("messages")
                        .document(it2.id)
                        .delete()
                }
            }
        MyFirebase().db.collection("conversation")
            .document(conId)
            .update("lastMsg", "(no message)")

    }

    fun deleteChat(conId: String, callback: Callback) {
        MyFirebase().db.collection("conversation")
            .document(conId)
            .delete()
            .addOnSuccessListener {
                callback.onDeleteSuccess(true)
            }
            .addOnFailureListener {
                callback.onDeleteSuccess(false)
            }
    }

    interface MessDataSourceCallback{
        fun onReceiveMessageList(listMes: List<Message>?)
        fun onReceiveConversation(conversation: Conversation)
    }

    interface Callback{
        fun onDeleteSuccess(isSuccess: Boolean)
    }
}