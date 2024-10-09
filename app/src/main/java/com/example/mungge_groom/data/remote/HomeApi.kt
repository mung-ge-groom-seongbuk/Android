package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.data.request.UpdateProfileDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.ui.base.BaseResponse
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface HomeApi {
    @GET("/matches")
    suspend fun getMatches(
        @Body matchDTO: MatchesDTO
    ): BaseResponse<String>

    @POST("/matches/send")
    suspend fun postSendMatches(
        @Body sendMatchDTO: SendMatchDTO
    ): BaseResponse<String>

    @GET("/notifications")
    suspend fun postNotifications(
        @Body notificationDTO: NotificationDTO
    ): BaseResponse<String>

    @POST("/notifications/respond")
    suspend fun postNotificationRespondDTO(
        @Body notificationRespondDTO: NotificationRespondDTO
    ): BaseResponse<ResultRespond>




}