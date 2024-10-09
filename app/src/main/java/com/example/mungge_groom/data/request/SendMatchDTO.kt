package com.example.mungge_groom.data.request

data class SendMatchDTO(
    val requester_id : String,
    val responder_id : String,
    val message : String
)
