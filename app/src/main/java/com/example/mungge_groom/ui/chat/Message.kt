package com.example.mungge_groom.ui.chat

data class Message(
    val chat_id: Int,
    val created_at: String,
    val message: String,
    val `receiver`: Receiver,
    val receiver_id: Int,
    val sender: Sender,
    val sender_id: Int
)