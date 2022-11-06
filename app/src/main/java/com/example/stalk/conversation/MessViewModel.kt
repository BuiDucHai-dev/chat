package com.example.stalk.conversation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.Conversation
import com.example.stalk.conversation.model.Message

class MessViewModel(private val repository: MessRepository, private val conId: String): ViewModel() {

    private val mListMess = MutableLiveData<List<Message>>()
    val listMess: LiveData<List<Message>> = mListMess

    private val mDeleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = mDeleteSuccess

    private val mCurrentConversation = MutableLiveData<Conversation>()
    val currentConversation: LiveData<Conversation> = mCurrentConversation

    fun getListMessage(){
        repository.getListMessage(conId, object : MessRepository.MessRepositoryCallback {
            override fun onReceiveMessageList(listMes: List<Message>?) {
                mListMess.value = listMes!!
            }

            override fun onReceiveConversation(conversation: Conversation) {
            }
        })
    }

    fun sendMess(content: String){
        repository.sendMess(conId, content)
    }

    fun deleteMessage(selectedList: ArrayList<Message>, isSelectAll: Boolean) {
        repository.deleteMessage(conId, selectedList, isSelectAll)
    }

    fun getConversation(conId: String?) {
        repository.getConversation(conId, object : MessRepository.MessRepositoryCallback{
            override fun onReceiveMessageList(listMes: List<Message>?) {
            }

            override fun onReceiveConversation(conversation: Conversation) {
                mCurrentConversation.value = conversation
            }
        })
    }

    fun clearHistory(conId: String) {
        repository.clearHistory(conId)
    }

    fun deleteChat(conId: String) {
        repository.deleteChat(conId, object : MessRepository.Callback{
            override fun onDeleteSuccess(isSuccess: Boolean) {
                mDeleteSuccess.value = isSuccess
            }
        })
    }
}