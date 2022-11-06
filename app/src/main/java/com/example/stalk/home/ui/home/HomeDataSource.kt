package com.example.stalk.home.ui.home

import android.util.Log
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.Conversation
import com.google.firebase.firestore.Query

class HomeDataSource {

    fun getConversationList(callback: HomeDataSourceCallback){
        MyFirebase().db.collection("conversation")
            .orderBy("dtmModify", Query.Direction.DESCENDING)
            .whereArrayContains("member", MyFirebase().auth.currentUser!!.uid)
            .addSnapshotListener{ snapshots, _ ->
                if (snapshots != null && !snapshots.isEmpty){
                    callback.onReceiveConversationList(snapshots.toObjects(Conversation::class.java))
                }
                else{
                    Log.d("hai.bd", "List conversation is null or empty")
                }
            }
    }

    interface HomeDataSourceCallback{
        fun onReceiveConversationList(listConversation: List<Conversation>)
    }
}