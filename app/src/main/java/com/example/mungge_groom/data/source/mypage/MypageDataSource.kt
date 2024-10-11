package com.example.mungge_groom.data.source.mypage

import android.util.Log
import com.example.mungge_groom.data.remote.ChatApi
import com.example.mungge_groom.data.remote.MypageApi
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.data.response.UserResponse
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MypageDataSource @Inject constructor(
    private val mypageApi: MypageApi
) {
    fun getUsers(): Flow<List<User>> = flow {
        val result = mypageApi.getUsers()
        emit(result)
    }.catch {
        Log.e("getUsers Failure", it.message.toString())
    }
}