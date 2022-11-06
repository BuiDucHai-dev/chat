package com.example.stalk.conversation.model

data class Message(
    var owner: String? = null,
    var content: String? = null,
    var conversationId: String? = null,
    var dtmRegister: String? = null,
    var msgId: String? = null,
    var state: String? = null,
    var isGroup: Boolean? = null,
    var isSelected: Boolean = false
)