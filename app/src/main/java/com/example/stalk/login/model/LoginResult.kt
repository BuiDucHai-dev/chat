package com.example.stalk.login.model


data class LoginResult (
    val sendOtpSuccess: String? = null,
    val errorGetOtp: String? = null,

    val verifySuccess: String? = null,
    val verifyError: String? = null,

    val createUserSuccess: String? = null,
    val createUserError: String? = null,

    val existedUser: String? = null,
    val notExistedUser: String? = null
)