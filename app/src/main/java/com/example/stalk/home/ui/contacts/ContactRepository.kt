package com.example.stalk.home.ui.contacts

import com.example.stalk.common.model.User

class ContactRepository(val dataSource: ContactDataSource) {

    fun getContactList(callback: ContactRepositoryCallback){
        dataSource.getContactList(object :ContactDataSource.ContactDataSourceCallback{
            override fun onReceiveContactList(listContact: List<User>, isByName: Boolean) {
                callback.onReceiveContactList(listContact, isByName)
            }
        })
    }
    fun getContactListByName(callback: ContactRepositoryCallback){
        dataSource.getContactListByName(object :ContactDataSource.ContactDataSourceCallback{
            override fun onReceiveContactList(listContact: List<User>, isByName: Boolean) {
                callback.onReceiveContactList(listContact, isByName)
            }
        })
    }

    fun getConversationId(userId: String, callback: ContactRepositoryConversationCallback) {
        dataSource.getConversationId(userId, object : ContactDataSource.ContactDataSourceConversationCallback{
            override fun onReceiverConversationId(conId: String) {
                callback.onReceiverConversationId(conId)
            }
        })
    }

    fun createNewConversation(selectedUser: User, currentUser: User, callback: ContactRepositoryCreateConversationCallback) {
        dataSource.createNewConversation(selectedUser, currentUser, object: ContactDataSource.ContactDataSourceCreateConversationCallback{
            override fun onCreateConversationSuccess(conId: String) {
                callback.onCreateConversationSuccess(conId)
            }

            override fun onCreateConversationFail() {
                callback.onCreateConversationFail()
            }

        })
    }

    interface ContactRepositoryCallback{
        fun onReceiveContactList(listContact: List<User>, isByName: Boolean)
    }

    interface ContactRepositoryConversationCallback{
        fun onReceiverConversationId(conId: String)
    }

    interface ContactRepositoryCreateConversationCallback{
        fun onCreateConversationSuccess(conId: String)
        fun onCreateConversationFail()
    }
}