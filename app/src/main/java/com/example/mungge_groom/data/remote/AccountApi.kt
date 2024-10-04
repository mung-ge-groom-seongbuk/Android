package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {

    @POST("/signup")
    suspend fun postSignUp(
        @Body signUpDTO: SignUpDTO
    ): BaseResponse<String>

    @POST("/login")
    suspend fun postLogIn(
        @Body logInDTO: LogInDTO
    ): BaseResponse<String>



}