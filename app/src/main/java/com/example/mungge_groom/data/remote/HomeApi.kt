package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.model.RunningData
import com.example.mungge_groom.data.request.DailyRunsDTO
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.response.DailySummaryResponse
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.response.DailyRuns
import com.example.mungge_groom.data.response.GetRecordRunData
import com.example.mungge_groom.data.response.MatchRequest
import com.example.mungge_groom.data.response.MatchResponseDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.ui.base.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HomeApi {
    @GET("/matches")
    suspend fun getMatches(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String,
    ): BaseResponse<String>

    @POST("/matches/send")
    suspend fun postSendMatches(
        @Body sendMatchDTO: SendMatchDTO
    ): BaseResponse<String>



    @GET("/notifications")
    suspend fun postNotifications(
    ): List<MatchRequest>

    @POST("/notifications/respond")
    suspend fun postNotificationRespondDTO(
        @Body notificationRespondDTO: NotificationRespondDTO
    ): BaseResponse<ResultRespond>

    @GET("/map")
    suspend fun getMap(
    ): BaseResponse<String>

    @POST("/running")
    suspend fun postRunning(
        @Body runningData: RunningData
    ): BaseResponse<String>

    @GET("running/{userId}")
    suspend fun getRunning(
        @Query("userId") userId: String
    ): GetRecordRunData

    @POST("daily/runs")
    suspend fun postDailyRun(
        @Body dailyRunsDTO: DailyRunsDTO
    ) : DailyRuns

    @GET("daily/summary/{userId}")
    suspend fun getDailySummary(
        @Query("userId") userId: String
    ): List<DailySummaryResponse>
}