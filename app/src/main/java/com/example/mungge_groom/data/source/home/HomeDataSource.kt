package com.example.mungge_groom.data.source.home

import android.util.Log
import com.example.mungge_groom.data.remote.ChatApi
import com.example.mungge_groom.data.remote.HomeApi
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val homeApi: HomeApi
) {
    fun getMatches(matchDTO: MatchesDTO): Flow<BaseResponse<String>> = flow {
        val result = homeApi.getMatches(matchDTO)
        emit(result)
    }.catch {
        Log.e("getMatches Failure", it.message.toString())
    }

    fun postSendMatches(sendMatchDTO: SendMatchDTO): Flow<BaseResponse<String>> = flow {
        val result = homeApi.postSendMatches(sendMatchDTO)
        emit(result)
    }.catch {
        Log.e("postSendMatches Failure", it.message.toString())
    }

    fun postNotifications(notificationDTO: NotificationDTO): Flow<BaseResponse<String>> = flow {
        val result = homeApi.postNotifications(notificationDTO)
        emit(result)
    }.catch {
        Log.e("postNotifications Failure", it.message.toString())
    }

    fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>> = flow {
        val result = homeApi.postNotificationRespondDTO(notificationRespondDTO)
        emit(result)
    }.catch {
        Log.e("postNotificationRespondDTO Failure", it.message.toString())
    }
}