package com.example.stalk.conversation

import com.example.stalk.common.model.Conversation
import com.example.stalk.conversation.model.Message

class MessRepository(private val dataSource: MessDataSource) {

    fun getListMessage(conId: String, callback: MessRepositoryCallback){
        dataSource.getListMessage(conId, object : MessDataSource.MessDataSourceCallback {
            override fun onReceiveMessageList(listMes: List<Message>?) {
                callback.onReceiveMessageList(listMes)
            }

            override fun onReceiveConversation(conversation: Conversation) {
            }
        })
    }

    fun sendMess(conversationId: String, content: String){
        dataSource.sendMess(conversationId, content)
    }

    fun deleteMessage(conId: String, selectedList: ArrayList<Message>, isSelectAll: Boolean) {
        dataSource.deleteMessage(conId, selectedList, isSelectAll)
    }

    fun getConversation(conId: String?, callback: MessRepositoryCallback) {
        dataSource.getConversation(conId, object : MessDataSource.MessDataSourceCallback{
            override fun onReceiveMessageList(listMes: List<Message>?) {
            }

            override fun onReceiveConversation(conversation: Conversation) {
                callback.onReceiveConversation(conversation)
            }
        })
    }

    fun clearHistory(conId: String) {
        dataSource.clearHistory(conId)
    }

    fun deleteChat(conId: String, callback: Callback) {
        dataSource.deleteChat(conId, object : MessDataSource.Callback{
            override fun onDeleteSuccess(isSuccess: Boolean) {
                callback.onDeleteSuccess(true)
            }
        })
    }

    interface MessRepositoryCallback{
        fun onReceiveMessageList(listMes: List<Message>?)
        fun onReceiveConversation(conversation: Conversation)
    }

    interface Callback{
        fun onDeleteSuccess(isSuccess: Boolean)
    }
}