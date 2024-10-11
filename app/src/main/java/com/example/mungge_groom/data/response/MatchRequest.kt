package com.example.mungge_groom.data.response

data class MatchRequest(
    val message: String = "",
    val requester: Requester= Requester()
)

data class Requester(
    val nickname: String= "",
    val intro: String= ""
)
