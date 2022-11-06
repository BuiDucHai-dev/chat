package com.example.stalk.login.model

import android.net.Uri

data class AccInitFormState(
    val firstnameError: Int? = null,
    val lastnameError: Int? = null,
    val avatarError: Int? = null,
    val isDataValid: Boolean = false
)
