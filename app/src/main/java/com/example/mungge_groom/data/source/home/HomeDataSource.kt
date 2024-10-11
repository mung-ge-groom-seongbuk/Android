package com.example.mungge_groom.data.source.home

import android.util.Log
import com.example.mungge_groom.data.model.RunningData
import com.example.mungge_groom.data.remote.HomeApi
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeDataSource @Inject constructor(
    private val homeApi: HomeApi
) {
    fun getMatches(
        latitude: String,
        longitude: String
    ): Flow<BaseResponse<String>> = flow {
        val result = homeApi.getMatches(latitude, longitude)
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

    fun postNotifications(
    ): Flow<List<MatchRequest>> = flow {
        val result = homeApi.postNotifications()
        emit(result)
    }.catch {
        Log.e("postNotifications Failure", it.message.toString())
    }

    fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO): Flow<BaseResponse<ResultRespond>> =
        flow {
            val result = homeApi.postNotificationRespondDTO(notificationRespondDTO)
            emit(result)
        }.catch {
            Log.e("postNotificationRespondDTO Failure", it.message.toString())
        }

    fun getMap(): Flow<BaseResponse<String>> =
        flow {
            val result = homeApi.getMap()
            emit(result)
        }.catch {
            Log.e("getMap Failure", it.message.toString())
        }

    //러닝 하루 기록
    fun postRunning(runningData: RunningData) : Flow<BaseResponse<String>> =
        flow{
            val result = homeApi.postRunning(runningData)
            emit(result)
        }.catch{
            Log.e("postRunning Failure", it.message.toString())
        }

    //러닝 조회
    fun getRunning(userId: String) : Flow<GetRecordRunData> =
        flow{
            val result = homeApi.getRunning(userId)
            emit(result)
        }.catch{
            Log.e("getRunning Failure", it.message.toString())
        }

//하루 총 러닝 기록
    fun postDailyRun(dailyRunsDTO: DailyRunsDTO) : Flow<DailyRuns> =
        flow{
            val result = homeApi.postDailyRun(dailyRunsDTO)
            emit(result)
        }.catch{
            Log.e("postRunning Failure", it.message.toString())
        }

    //총 러닝 조회
    fun getDailySummary(userId: String) : Flow<List<DailySummaryResponse>> =
        flow{
            val result = homeApi.getDailySummary(userId)
            emit(result)
        }.catch{
            Log.e("postRunning Failure", it.message.toString())
        }



}