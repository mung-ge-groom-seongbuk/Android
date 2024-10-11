package com.example.mungge_groom.data.response

data class LoginResponse(
    val message : String? = null,
    val token : String?= null,
    val redirectUrl : String?= null
)
