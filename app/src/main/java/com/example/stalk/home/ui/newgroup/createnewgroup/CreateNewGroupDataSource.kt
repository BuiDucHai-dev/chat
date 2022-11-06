package com.example.stalk.home.ui.newgroup.createnewgroup

import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.Conversation
import com.example.stalk.common.model.User

class CreateNewGroupDataSource {
    fun createNewConversation(list: List<User>, user: User, avatar: String, title: String, callback: Callback) {
        var listUser: List<String> = ArrayList()
        for (l in list){
            listUser = listUser.plus(l.userId.toString())
        }
        listUser = listUser.plus(user.userId.toString())

        val conversation = Conversation(
            avatar = listOf(avatar),
            conId = "newConversation",
            dtmModify = Comm().getCurrentTime(),
            group = true,
            lastMsg = "",
            member = listUser,
            title = listOf(title),
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

    interface Callback{
        fun onCreateConversationSuccess(conId: String)
        fun onCreateConversationFail()
    }
}