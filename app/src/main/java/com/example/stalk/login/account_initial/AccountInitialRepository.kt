package com.example.stalk.login.account_initial

import android.net.Uri

class AccountInitialRepository(private val dataSource: AccountInitialDataSource) {

    fun createUserData(etFirstName: String, etLastName: String, uriImage: Uri, phoneNum: String, callback: AccountInitialRepositoryCallback) {
        dataSource.createUserData(etFirstName, etLastName, uriImage, phoneNum, object : AccountInitialDataSource.AccountInitialDataSourceCallback{
            override fun onCreateUserFailure() {
                callback.onCreateUserFailure()
            }

            override fun onCreateUserSuccess() {
                callback.onCreateUserSuccess()
            }
        })
    }

    interface AccountInitialRepositoryCallback{
        fun onCreateUserSuccess()
        fun onCreateUserFailure()
    }
}