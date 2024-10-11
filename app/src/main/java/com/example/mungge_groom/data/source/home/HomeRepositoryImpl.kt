package com.example.mungge_groom.data.source.home

import com.example.mungge_groom.data.model.RunningData
import com.example.mungge_groom.data.repository.HomeRepository
import com.example.mungge_groom.data.request.DailyRunsDTO
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.response.DailyRuns
import com.example.mungge_groom.data.response.DailySummaryResponse
import com.example.mungge_groom.data.response.GetRecordRunData
import com.example.mungge_groom.data.response.MatchRequest
import com.example.mungge_groom.data.response.MatchResponseDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
    override suspend fun getMatches(
        latitude: String,
        longitude: String
    ): Flow<BaseResponse<String>> = homeDataSource.getMatches(latitude, longitude)

    override suspend fun postSendMatches(sendMatchDTO: SendMatchDTO): Flow<BaseResponse<String>> =
        homeDataSource.postSendMatches(sendMatchDTO)

    override suspend fun postNotifications(
    ): Flow<List<MatchRequest>> =
        homeDataSource.postNotifications()

    override suspend fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>> =
        homeDataSource.postNotificationRespondDTO(notificationRespondDTO)

    override suspend fun getMap(): Flow<BaseResponse<String>> = homeDataSource.getMap()

    override suspend fun postRunning(runningData: RunningData): Flow<BaseResponse<String>> = homeDataSource.postRunning(runningData)

    override suspend fun getRunning(userId: String): Flow<GetRecordRunData> = homeDataSource.getRunning(userId)

    override suspend fun postDailyRun(dailyRunsDTO: DailyRunsDTO): Flow<DailyRuns> = homeDataSource.postDailyRun(dailyRunsDTO)

    override suspend fun getDailySummary(userId: String): Flow<List<DailySummaryResponse>> = homeDataSource.getDailySummary(userId)

}