package com.example.stalk.login.login

import android.app.Activity
import com.example.stalk.common.firebase.MyFirebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFrgDataSource {

    fun login(activity: Activity, phoneNum: String, callback: LoginFrgDataSourceCallback){
        val options = PhoneAuthOptions.newBuilder(MyFirebase().auth)
            .setPhoneNumber(phoneNum)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    callback.onReceiveVerificationId(p0)
                }

                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    callback.onVerificationFailed()
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    interface LoginFrgDataSourceCallback{
        fun onReceiveVerificationId(verificationId: String)
        fun onVerificationFailed()
    }
}