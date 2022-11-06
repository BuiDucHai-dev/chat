package com.example.stalk.common.model

data class User(
    var avatar: String? = null,
    var dtmLastSeen: String? = null,
    var first: String? = null,
    var friend: ArrayList<String>? = null,
    var fullName: String? = null,
    var last: String? = null,
    var userId: String? = null,
    var phoneNum: String? = null,
    var bio: String? = null
)