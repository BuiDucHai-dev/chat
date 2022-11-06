package com.example.stalk.login.verification

class VerificationRepository(private val dataSource: VerificationDataSource) {

    fun signInWithPhoneAuthCredential(verificationId: String, userVerify: String, callback:VerificationRepositoryCallback) {
        dataSource.signInWithPhoneAuthCredential(verificationId, userVerify, object :VerificationDataSource.VerificationDataSourceCallBack{
            override fun onSignInSuccess(isExistedUser: Boolean) {
                callback.onSignInSuccess(isExistedUser)
            }
            override fun onSignInFail() {
                callback.onSignInFail()
            }
        })
    }

    interface VerificationRepositoryCallback{
        fun onSignInSuccess(isExistedUser: Boolean)
        fun onSignInFail()
    }
}