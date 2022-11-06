package com.example.stalk.common.model

data class Conversation(
    var avatar: List<String?>,
    var conId: String?,
    var dtmModify: String?,
    var group: Boolean?,
    var lastMsg: String?,
    var member: List<String?>,
    var title: List<String?>,
    var read: List<String?>
){
    constructor() : this(emptyList(), "", "", false, "", emptyList(), emptyList(), emptyList())
}