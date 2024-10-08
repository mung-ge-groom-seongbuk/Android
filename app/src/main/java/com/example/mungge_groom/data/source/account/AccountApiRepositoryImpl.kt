package com.example.mungge_groom.data.source.account

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AccountApiRepositoryImpl @Inject constructor(
    private val accountDataSource: AccountApiDataSource
) : AccountRepository {
    override suspend fun postLogIn(logInDTO: LogInDTO): Flow< BaseResponse<String>> = accountDataSource.postLogIn(logInDTO)
    override suspend fun postSignUp(signUpDTO: SignUpDTO): Flow<BaseResponse<String>> = accountDataSource.postSignUp(signUpDTO)
    override suspend fun postUpdateProfile(
        profile_picture: MultipartBody.Part,
        intro: RequestBody,
        nickname : RequestBody,
        email: RequestBody
    ) : Flow<BaseResponse<String>> = accountDataSource.postUpdateProfile(profile_picture, intro, nickname, email)
}