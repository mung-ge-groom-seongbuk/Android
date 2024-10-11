package com.example.mungge_groom.data.response

data class MatchResponseDTO(
    val created_at: String = "",
    val match_id : String = "",
    val requester_id : String = "",
    val responder_id : String = "",
    val message: String = "",
    val status : String = ""
)