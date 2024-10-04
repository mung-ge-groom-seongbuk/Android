package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun postLogIn(logInDTO: LogInDTO): Flow<BaseResponse<String>>
    suspend fun postSignUp(signUpDTO: SignUpDTO): Flow<BaseResponse<String>>
}
