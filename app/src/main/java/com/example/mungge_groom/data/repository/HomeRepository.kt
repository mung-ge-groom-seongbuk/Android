package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.data.response.ResultRespond
import com.example.mungge_groom.ui.base.BaseResponse
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface HomeRepository {
    suspend fun getMatches(matchesDTO: MatchesDTO): Flow<BaseResponse<String>>
    suspend fun postSendMatches(sendMatchDTO: SendMatchDTO): Flow<BaseResponse<String>>
    suspend fun postNotifications(notificationDTO: NotificationDTO): Flow<BaseResponse<String>>
    suspend fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>>
}
