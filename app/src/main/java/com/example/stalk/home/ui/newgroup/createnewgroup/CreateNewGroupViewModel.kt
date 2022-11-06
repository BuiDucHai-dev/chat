package com.example.stalk.home.ui.newgroup.createnewgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.User

class CreateNewGroupViewModel(val repository: CreateNewGroupRepository): ViewModel()  {

    private val mConversationId = MutableLiveData<String>()
    val conversationId: LiveData<String> = mConversationId

    fun createNewConversation(list: List<User>, user: User, avatar: String, title: String) {
        repository.createNewConversation(list, user, avatar, title, object: CreateNewGroupRepository.Callback{
            override fun onCreateConversationSuccess(conId: String) {
                mConversationId.value = conId
            }

            override fun onCreateConversationFail() {
            }

        })
    }

}