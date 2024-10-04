package com.example.mungge_groom.data.source

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountApiRepositoryImpl @Inject constructor(
    private val accountDataSource: AccountApiDataSource
) : AccountRepository {
    override suspend fun postLogIn(logInDTO: LogInDTO): Flow< BaseResponse<String>> = accountDataSource.postLogIn(logInDTO)
    override suspend fun postSignUp(signUpDTO: SignUpDTO): Flow<BaseResponse<String>> = accountDataSource.postSignUp(signUpDTO)
}