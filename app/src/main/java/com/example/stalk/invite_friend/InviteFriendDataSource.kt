package com.example.stalk.invite_friend

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import com.example.stalk.invite_friend.model.PhoneContact

class InviteFriendDataSource {

    @SuppressLint("Range", "Recycle")
    fun getContactList(context: Context, callback: Callback, ) {

        var contactList = ArrayList<PhoneContact>()
        val contact = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            , null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC")
        if (contact != null) {
            while (contact.moveToNext()){
                val c = PhoneContact()
                c.name = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                c.number = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                c.uri = contact.getString(contact.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                contactList.add(c)
            }
            callback.onReceiveContactList(contactList)
        } else {
            callback.onReceiveContactList(ArrayList())
        }

    }

    interface Callback{
        fun onReceiveContactList(list: ArrayList<PhoneContact>)
    }
}