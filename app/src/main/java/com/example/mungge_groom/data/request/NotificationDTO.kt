package com.example.mungge_groom.data.request

data class NotificationDTO(
    val match_id : String,
    val requester : String,
    val nickname : String,
    val intro : String,
    val message : String
)
