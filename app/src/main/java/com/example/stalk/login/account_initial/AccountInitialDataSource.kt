package com.example.stalk.login.account_initial

import android.net.Uri
import android.util.Log
import com.example.stalk.common.Comm
import com.example.stalk.common.firebase.MyFirebase

class AccountInitialDataSource {

    fun createUserData(etFirstName: String, etLastName: String, uriImage: Uri, phoneNum: String, callback: AccountInitialDataSourceCallback) {
        val data = hashMapOf(
            "avatar" to uriImage.toString(),
            "first" to etFirstName,
            "last" to etLastName,
            "userId" to MyFirebase().auth.currentUser!!.uid,
            "fullName" to "$etFirstName $etLastName",
            "dtmLastSeen" to Comm().getCurrentTime(),
            "friend" to arrayListOf<String>(),
            "phoneNum" to phoneNum,
            "bio" to ""
        )

        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser!!.uid)
            .set(data)
            .addOnSuccessListener {
                uploadAvatar(uriImage, callback)
            }
            .addOnFailureListener {
                callback.onCreateUserFailure()
            }
    }

    private fun uploadAvatar(uriImage: Uri, callback: AccountInitialDataSourceCallback) {
        MyFirebase().storageRef.child("user/" + MyFirebase().auth.currentUser!!.uid)
            .putFile(uriImage)
            .continueWithTask {
                MyFirebase().storageRef.child("user/" + MyFirebase().auth.currentUser!!.uid).downloadUrl
            }
            .addOnSuccessListener {
                Log.d("hai.bd", "upload avatar success")
                callback.onCreateUserSuccess()
            }
            .addOnFailureListener {
                Log.d("hai.bd", "upload avatar fail")
                callback.onCreateUserFailure()
            }
            .addOnCompleteListener{
                setAvatar(it.result.toString())
            }
    }

    private fun setAvatar(uri: String) {
        MyFirebase().db.collection("users")
            .document(MyFirebase().auth.currentUser!!.uid)
            .update("avatar", uri)
    }

    interface AccountInitialDataSourceCallback{
        fun onCreateUserSuccess()
        fun onCreateUserFailure()
    }
}