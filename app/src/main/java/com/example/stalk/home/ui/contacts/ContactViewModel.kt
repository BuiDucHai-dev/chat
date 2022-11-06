package com.example.stalk.home.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.User

class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val mListContact = MutableLiveData<List<User>>()
    val listContact: LiveData<List<User>> = mListContact

    private val mSortByName = MutableLiveData<Boolean>()
    val sortType: LiveData<Boolean> = mSortByName

    private val mConversationId = MutableLiveData<String>()
    val conversationId: LiveData<String> = mConversationId

    fun getContactList(){
        repository.getContactList(object : ContactRepository.ContactRepositoryCallback{
            override fun onReceiveContactList(listContact: List<User>, isByName: Boolean){
                if (mSortByName.value == isByName){
                    mListContact.value = listContact
                }
            }
        })
    }

    fun getContactListByName(){
        repository.getContactListByName(object : ContactRepository.ContactRepositoryCallback{
            override fun onReceiveContactList(listContact: List<User>, isByName: Boolean){
                mListContact.value = listContact
                if (mSortByName.value == isByName){
                    mListContact.value = listContact
                }
            }
        })
    }

    fun changeSortType(){
        mSortByName.value = mSortByName.value == false
    }

    fun contactFilter(p0: String?) : List<User>?{
        return this.mListContact.value?.filter {
            it.fullName!!.startsWith(p0.toString())
        }
    }

    fun getConversationId(userId: String) {
        repository.getConversationId(userId, object : ContactRepository.ContactRepositoryConversationCallback{
            override fun onReceiverConversationId(conId: String) {
                mConversationId.value = conId
            }
        })
    }

    fun createNewConversation(selectedUser: User, currentUser: User) {
        repository.createNewConversation(selectedUser, currentUser, object : ContactRepository.ContactRepositoryCreateConversationCallback{
            override fun onCreateConversationSuccess(conId: String) {
                mConversationId.value = conId
            }

            override fun onCreateConversationFail() {
            }
        })
    }

}