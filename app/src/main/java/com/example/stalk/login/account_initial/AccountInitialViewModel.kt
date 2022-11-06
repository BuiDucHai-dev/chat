package com.example.stalk.login.account_initial

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stalk.R
import com.example.stalk.login.model.AccInitFormState

class AccountInitialViewModel(private val repository: AccountInitialRepository) :ViewModel() {

    private val mFormState = MutableLiveData<AccInitFormState>()
    val formState: LiveData<AccInitFormState> = mFormState

    private val mInitUserResult = MutableLiveData<String>()
    val initUserResult: LiveData<String> = mInitUserResult

    fun getStatusForm(firstname: String, lastname: String, hasImage: Boolean) {
        when {
            firstname.isEmpty() -> {
                mFormState.value = AccInitFormState(firstnameError = R.string.invalid_firstname)
            }
            lastname.isEmpty() -> {
                mFormState.value = AccInitFormState(lastnameError = R.string.invalid_lastname)
            }
            !hasImage -> {
                mFormState.value = AccInitFormState(avatarError = R.string.invalid_image)
            }
            else -> {
                mFormState.value = AccInitFormState(isDataValid = true)
            }
        }
    }

    fun createUserData(etFirstName: String, etLastName: String, uriImage: Uri, phoneNum: String) {
        repository.createUserData(etFirstName, etLastName, uriImage, phoneNum, object : AccountInitialRepository.AccountInitialRepositoryCallback{
            override fun onCreateUserSuccess() {
                mInitUserResult.value = "success"
            }

            override fun onCreateUserFailure() {
                mInitUserResult.value = "fail"
            }
        })
    }
}