package com.example.stalk.login.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginFrgViewModel(private val repository: LoginFrgRepository): ViewModel() {

    private val mLoginVerificationId = MutableLiveData<String>()
    var loginVerificationId: LiveData<String> = mLoginVerificationId


    fun login(activity: Activity, phoneNum: String){
        repository.login(activity, phoneNum, object: LoginFrgRepository.LoginFrgRepositoryCallback{
            override fun onReceiveVerificationId(verificationId: String) {
                mLoginVerificationId.value = verificationId
            }

            override fun onVerificationFailed() {
                mLoginVerificationId.value = "getOtpFail"
            }
        })
    }

}