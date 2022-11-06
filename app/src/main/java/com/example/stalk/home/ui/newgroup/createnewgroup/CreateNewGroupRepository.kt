package com.example.stalk.home.ui.newgroup.createnewgroup

import com.example.stalk.common.model.User

class CreateNewGroupRepository(val dataSource: CreateNewGroupDataSource)  {

    fun createNewConversation(list: List<User>, user: User, avatar: String, title: String, callback: Callback) {
        dataSource.createNewConversation(list, user, avatar, title, object: CreateNewGroupDataSource.Callback{
            override fun onCreateConversationSuccess(conId: String) {
                callback.onCreateConversationSuccess(conId)
            }

            override fun onCreateConversationFail() {
                callback.onCreateConversationFail()
            }

        })
    }

    interface Callback{
        fun onCreateConversationSuccess(conId: String)
        fun onCreateConversationFail()
    }
}