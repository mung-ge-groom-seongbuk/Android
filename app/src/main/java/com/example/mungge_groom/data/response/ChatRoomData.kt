package com.example.mungge_groom.data.response

import com.example.mungge_groom.ui.chat.Message

data class ChatRoomData(
    val success: String = "",
    val messages: List<ChatRoom> = emptyList()
)
