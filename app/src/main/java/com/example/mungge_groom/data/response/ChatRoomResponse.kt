package com.example.mungge_groom.data.response

data class ChatRoomResponse(
    val success : String = "",
    val message : String = "",
    val chatMessage : ChatMessage =  ChatMessage()
)

