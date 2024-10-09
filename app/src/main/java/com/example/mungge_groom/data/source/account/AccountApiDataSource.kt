package com.example.mungge_groom.data.source.account

import android.util.Log
import com.example.mungge_groom.data.remote.AccountApi
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    fun postUpdateProfile(profile_picture : MultipartBody.Part, intro : RequestBody, nickname: RequestBody ,email : RequestBody) : Flow<BaseResponse<String>> = flow {
        val result = accountApi.postUpdateProfile(profile_picture,intro,nickname,email)
        emit(result)
    }.catch {
        Log.e("Post Update Profile Failure", it.message.toString())
    }

}
