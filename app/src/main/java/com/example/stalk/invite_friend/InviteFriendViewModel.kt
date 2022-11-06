package com.example.stalk.invite_friend

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.R
import com.example.stalk.invite_friend.model.PhoneContact

class InviteFriendViewModel(private val repository: InviteFriendRepository): ViewModel() {

    private val mListContact = MutableLiveData<ArrayList<PhoneContact>>()
    val listContact: LiveData<ArrayList<PhoneContact>> = mListContact

    fun getContactList(context: Context) {
        repository.getContactList(context, object : InviteFriendRepository.Callback{
            override fun onReceiveContactList(list: ArrayList<PhoneContact>) {
                mListContact.value = list
            }
        })
    }

    fun moveToSendMessage(activity: InviteFriendActivity, selectedList: ArrayList<PhoneContact>) {
        if (selectedList.size == 0) return
        val userList = getUserList(selectedList)
        userList.filter{ !it.isWhitespace() }
        Log.d("hai.bd", userList)
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", userList)
        smsIntent.putExtra("sms_body", activity.getString(R.string.welcome_to_stalk))
        activity.startActivity(smsIntent)
    }

    private fun getUserList(selectedList: ArrayList<PhoneContact>) : String{
        val userList = StringBuilder(selectedList[0].number)
        selectedList.remove(selectedList[0])
        for (s in selectedList){
            userList.append(";" + s.number)
        }
        return userList.toString()
    }
}