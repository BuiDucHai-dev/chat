package com.example.stalk.login.verification

import android.util.Log
import com.example.stalk.common.firebase.MyFirebase
import com.google.firebase.auth.PhoneAuthProvider

class VerificationDataSource {
    fun signInWithPhoneAuthCredential(verificationId: String, userVerify: String, callBack: VerificationDataSourceCallBack) {
        val credential = PhoneAuthProvider.getCredential(verificationId, userVerify)
        MyFirebase().auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    checkExistedUser(object : VerificationDataSourceCallBack{
                        override fun onSignInSuccess(isExistedUser: Boolean) {
                            callBack.onSignInSuccess(true)
                        }
                        override fun onSignInFail() {
                            callBack.onSignInSuccess(false)
                        }
                    })
                    Log.d("hai.bd", "signInWithCredential:success")
                }else{
                    callBack.onSignInFail()
                    Log.w("hai.bd", "signInWithCredential:failure", it.exception)
                }
            }
    }

    private fun checkExistedUser(callBack: VerificationDataSourceCallBack){
        MyFirebase().db.collection("users")
            .whereEqualTo("userId", MyFirebase().auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty){
                    callBack.onSignInSuccess(true)
                }
                else {
                    callBack.onSignInFail()
                }
            }
            .addOnFailureListener{
                callBack.onSignInFail()
            }
    }
    interface VerificationDataSourceCallBack{
        fun onSignInSuccess(isExistedUser: Boolean)
        fun onSignInFail()
    }
}