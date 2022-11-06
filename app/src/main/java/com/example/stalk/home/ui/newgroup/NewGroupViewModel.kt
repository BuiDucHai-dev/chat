package com.example.stalk.home.ui.newgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.model.User

class NewGroupViewModel(val repository: NewGroupRepository): ViewModel() {

    private val mListUser = MutableLiveData<List<User>>()
    var listUser: LiveData<List<User>> = mListUser

    private val mListPick = MutableLiveData<List<User>>()
    var listPick: LiveData<List<User>> = mListPick

    fun getUserList() {
        repository.getUserList(object : NewGroupRepository.NewGroupRepositoryCallback{
            override fun onReceiverUserList(listUser: MutableList<User>) {
                mListUser.value = listUser
            }
        })
    }

    fun addToPickList(user: User) {
        if (mListPick.value == null){
            mListPick.value = ArrayList()
        }
        mListPick.value = mListPick.value?.plus(user)
        mListUser.value = mListUser.value?.minus(user)
    }

    fun removeFromPickList(user: User) {
        mListPick.value = mListPick.value?.minus(user)
        mListUser.value = mListUser.value?.plus(user)
    }
}