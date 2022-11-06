package com.example.stalk.login.verification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VerificationViewModel(private val repository: VerificationRepository): ViewModel() {

    companion object{
        const val VERIFICATION_RESULT_EXISTED = "existed"
        const val VERIFICATION_RESULT_NON_EXISTED = "non_existed"
        const val VERIFICATION_RESULT_FAIL = "fail2verify"
    }


    private val mVerificationResult = MutableLiveData<String>()
    val verificationResult: LiveData<String> = mVerificationResult

    fun signInWithPhoneAuthCredential(verificationId: String, userVerify: String){
        repository.signInWithPhoneAuthCredential(verificationId, userVerify, object : VerificationRepository.VerificationRepositoryCallback{
            override fun onSignInSuccess(isExistedUser: Boolean) {
                if (isExistedUser){
                    mVerificationResult.value = VERIFICATION_RESULT_EXISTED
                } else {
                    mVerificationResult.value = VERIFICATION_RESULT_NON_EXISTED
                }
            }
            override fun onSignInFail() {
                mVerificationResult.value = VERIFICATION_RESULT_FAIL
            }
        })
    }
}