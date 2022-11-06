package com.example.stalk.home.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.Conversation

class HomeViewModel(private val repository: HomeRepository) : ViewModel(){

    private val _listConversation = MutableLiveData<List<Conversation>>()
    val list: LiveData<List<Conversation>> = _listConversation

    fun getConversationList(){
        repository.getConversationList(object: HomeRepository.HomeRepositoryCallback{
            override fun onReceiveConversationList(listConversation: List<Conversation>) {
                _listConversation.value = listConversation
            }
        })
    }

}