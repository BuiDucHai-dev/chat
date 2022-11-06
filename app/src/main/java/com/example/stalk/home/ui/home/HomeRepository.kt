package com.example.stalk.home.ui.home

import com.example.stalk.common.model.Conversation

class HomeRepository(val dataSource: HomeDataSource) {

    fun getConversationList(callback: HomeRepositoryCallback){
        dataSource.getConversationList(object : HomeDataSource.HomeDataSourceCallback{
            override fun onReceiveConversationList(listConversation: List<Conversation>) {
                callback.onReceiveConversationList(listConversation)
            }
        })
    }

    interface HomeRepositoryCallback{
        fun onReceiveConversationList(listConversation: List<Conversation>)
    }
}