package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface AccountRepository {
    suspend fun postLogIn(logInDTO: LogInDTO): Flow<BaseResponse<String>>
    suspend fun postSignUp(signUpDTO: SignUpDTO): Flow<BaseResponse<String>>
    suspend fun postUpdateProfile(profile_picture : MultipartBody.Part,intro : RequestBody, nickname: RequestBody, email : RequestBody) : Flow<BaseResponse<String>>
}
