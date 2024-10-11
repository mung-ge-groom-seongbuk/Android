package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.data.request.UpdateProfileDTO
import com.example.mungge_groom.data.response.LoginResponse
import com.example.mungge_groom.ui.base.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AccountApi {

    @POST("/signup")
    suspend fun postSignUp(
        @Body signUpDTO: SignUpDTO
    ): BaseResponse<String>

    @POST("/login")
    suspend fun postLogIn(
        @Body logInDTO: LogInDTO
    ): LoginResponse

    @Multipart
    @POST("/updateProfile")
    suspend fun postUpdateProfile(
        @Part profile_picture : MultipartBody.Part,
        @Part("intro") intro : RequestBody,
        @Part("nickname") nickname : RequestBody,
        @Part("email") email : RequestBody
    ) : BaseResponse<String>

}