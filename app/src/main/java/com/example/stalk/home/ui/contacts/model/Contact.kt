package com.example.stalk.home.ui.contacts.model

data class Contact (
    var fullName: String? = null,
    var isOnline: Boolean? = false,
    var dtmLastSeen: String? = null
)