package com.example.mungge_groom.data.response

data class ChatRoom(
    val chat_id: String ="",
    val created_at: String="",
    val message: String="",
    val receiver_id: String="",
    val sender_id: String="",
    val sender : Sender?= Sender(),
    val receiver : Reiceiver=Reiceiver()
)