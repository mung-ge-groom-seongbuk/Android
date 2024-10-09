package com.example.mungge_groom.data.source.home

import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.repository.HomeRepository
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.data.source.chat.ChatDataSource
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override suspend fun getMatches(matchesDTO: MatchesDTO): Flow<BaseResponse<String>>
    = homeDataSource.getMatches(matchesDTO)
    override suspend fun postSendMatches(sendMatchDTO: SendMatchDTO): Flow<BaseResponse<String>>
    = homeDataSource.postSendMatches(sendMatchDTO)
    override suspend fun postNotifications(notificationDTO: NotificationDTO): Flow<BaseResponse<String>>
    = homeDataSource.postNotifications(notificationDTO)
    override suspend fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>>
    = homeDataSource.postNotificationRespondDTO(notificationRespondDTO)
}