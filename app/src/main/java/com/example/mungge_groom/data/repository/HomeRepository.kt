package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.model.RunningData
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

interface HomeRepository {
    suspend fun getMatches(
        latitude: String,
        longitude: String
    ): Flow<BaseResponse<String>>

    suspend fun postSendMatches(sendMatchDTO: SendMatchDTO): Flow<BaseResponse<String>>
    suspend fun postNotifications(
    ): Flow<List<MatchRequest>>

    suspend fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>>

    suspend fun getMap(): Flow<BaseResponse<String>>

    suspend fun postRunning(runningData : RunningData) : Flow<BaseResponse<String>>

    suspend fun getRunning(userId : String) : Flow<GetRecordRunData>

    suspend fun postDailyRun(dailyRunsDTO : DailyRunsDTO) : Flow<DailyRuns>

    suspend fun getDailySummary(userId : String) : Flow<List<DailySummaryResponse>>


}
