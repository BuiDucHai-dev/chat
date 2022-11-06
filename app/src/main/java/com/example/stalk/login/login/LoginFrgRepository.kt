package com.example.stalk.login.login

import android.app.Activity

class LoginFrgRepository(private val dataSource: LoginFrgDataSource) {

    fun login(activity: Activity, phoneNum: String, callback: LoginFrgRepositoryCallback){
        dataSource.login(activity, phoneNum, object : LoginFrgDataSource.LoginFrgDataSourceCallback{
            override fun onReceiveVerificationId(verificationId: String) {
                callback.onReceiveVerificationId(verificationId)
            }

            override fun onVerificationFailed() {
                callback.onVerificationFailed()
            }
        })
    }

    interface LoginFrgRepositoryCallback{
        fun onReceiveVerificationId(verificationId: String)
        fun onVerificationFailed()
    }
}