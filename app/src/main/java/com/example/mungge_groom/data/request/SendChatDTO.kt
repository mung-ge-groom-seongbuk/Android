package com.example.mungge_groom.data.request

data class SendChatDTO(
    val sender_id : String,
    val receiver_id : String,
    val message : String
)
