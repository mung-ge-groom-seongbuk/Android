package com.example.mungge_groom.data.response

data class UserResponse(
    val created_at: String,
    val email: String,
    val intro: Any,
    val name: String,
    val nickname: String,
    val password: String,
    val phone_number: String,
    val profile_picture: Any,
    val token: Any,
    val user_id: Int
)