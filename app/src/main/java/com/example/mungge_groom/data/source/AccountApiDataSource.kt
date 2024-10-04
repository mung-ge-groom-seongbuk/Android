package com.example.mungge_groom.data.source

import android.util.Log
import com.example.mungge_groom.data.remote.AccountApi
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountApiDataSource @Inject constructor(
    private val accountApi: AccountApi
) {
    //POST 로그인
    fun postLogIn(logInDTO: LogInDTO): Flow<BaseResponse<String>> = flow {
        val result = accountApi.postLogIn(logInDTO)
        emit(result)
    }.catch {
        Log.e("Post LogIn Failure", it.message.toString())
    }

    //POST 회원가입
    fun postSignUp(signUpDTO: SignUpDTO): Flow< BaseResponse<String>> = flow {
        val result = accountApi.postSignUp(signUpDTO)
        emit(result)
    }.catch {
        Log.e("Post LogIn Failure", it.message.toString())
    }

}
