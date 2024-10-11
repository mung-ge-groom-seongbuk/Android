package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.data.response.UserResponse
import com.example.mungge_groom.ui.base.BaseResponse
import retrofit2.http.GET

interface MypageApi {
    @GET("/users")
    suspend fun getUsers(
    ): List<User>
}