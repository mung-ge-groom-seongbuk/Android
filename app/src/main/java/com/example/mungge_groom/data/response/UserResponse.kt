package com.example.mungge_groom.data.response

data class UserResponse(
    val users: List<User>
)

data class User(
    val created_at: String,
    val email: String,
    val intro: String?,
    val name: String,
    val nickname: String,
    val password: String,
    val phone_number: String,
    var profile_picture: String?,
    val token: String?,
    val user_id: Int
)