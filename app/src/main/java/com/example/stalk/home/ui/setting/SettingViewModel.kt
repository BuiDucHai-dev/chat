package com.example.stalk.home.ui.setting

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.common.firebase.MyFirebase
import com.example.stalk.common.model.User

class SettingViewModel : ViewModel() {

    private val mUser = MutableLiveData<User>()
    val user: LiveData<User> = mUser

    fun getCurrentUser() {
        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser?.uid.toString())
            .addSnapshotListener{snapshots, _ ->
                if (snapshots != null) {
                    mUser.value = snapshots.toObject(User::class.java)
                }
            }
    }

    fun updateProfile(options: Int, content: String) {
        when (options){
            SettingFragment.SELECT_NAME -> {
               MyFirebase().db.collection("users")
                   .document(MyFirebase().auth.currentUser?.uid.toString())
                   .update("fullName", content)
            }
            SettingFragment.SELECT_BIO -> {
                MyFirebase().db.collection("users")
                    .document(MyFirebase().auth.currentUser?.uid.toString())
                    .update("bio", content)
            }
        }
    }

    fun updateAvatar(uriImage: Uri) {
        MyFirebase().storageRef.child("user/" + MyFirebase().auth.currentUser!!.uid)
            .putFile(uriImage)
            .continueWithTask {
                MyFirebase().storageRef.child("user/" + MyFirebase().auth.currentUser!!.uid).downloadUrl
            }
            .addOnCompleteListener {
                setAvatar(it.result.toString())
            }
    }

    private fun setAvatar(uri: String) {
        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser!!.uid)
            .update("avatar", uri)
    }

}