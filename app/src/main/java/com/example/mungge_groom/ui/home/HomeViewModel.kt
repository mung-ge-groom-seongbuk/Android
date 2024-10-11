package com.example.mungge_groom.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _matches = MutableStateFlow(BaseResponse<String>())
    val matches: StateFlow<BaseResponse<String>> = _matches

    private val _sendMatches = MutableStateFlow(BaseResponse<String>())
    val sendMatches: StateFlow<BaseResponse<String>> = _sendMatches

    private val _notifications = MutableStateFlow(emptyList<MatchRequest>())
    val notifications: StateFlow<List<MatchRequest>> = _notifications

    private val _notificationsRespond = MutableStateFlow(BaseResponse<ResultRespond>())
    val notificationsRespond: StateFlow<BaseResponse<ResultRespond>> = _notificationsRespond

    private val _map = MutableStateFlow(BaseResponse<String>())
    val map: StateFlow<BaseResponse<String>> = _map


    private val _postRunning = MutableStateFlow(BaseResponse<String>())
    val postRunning: StateFlow<BaseResponse<String>> = _postRunning

    private val _getRunning = MutableStateFlow(GetRecordRunData())
    val getRunning: StateFlow<GetRecordRunData> = _getRunning

    private val _postDailyRun = MutableStateFlow(DailyRuns())
    val postDailyRun: StateFlow<DailyRuns> = _postDailyRun

    private val _getDailySummary = MutableStateFlow(emptyList<DailySummaryResponse>())
    val getDailySummary: StateFlow<List<DailySummaryResponse>> = _getDailySummary


    fun postRunning(runningData: RunningData) {
        viewModelScope.launch {
            try {
                homeRepository.postRunning(runningData).collect {
                    _postRunning.value = it
                }
            } catch (e: Exception) {
                Log.e("Map Error", e.message.toString())
            }
        }
    }

    fun getRunning(userId: String) {
        viewModelScope.launch {
            try {
                homeRepository.getRunning(userId).collect {
                    _getRunning.value = it
                }
            } catch (e: Exception) {
                Log.e("Map Error", e.message.toString())
            }
        }
    }

    fun postDailyRun(dailyRunsDTO: DailyRunsDTO) {
        viewModelScope.launch {
            try {
                homeRepository.postDailyRun(dailyRunsDTO).collect {
                    _postDailyRun.value = it
                }
            } catch (e: Exception) {
                Log.e("Map Error", e.message.toString())
            }
        }
    }

    fun getDailySummary(userId: String) {
        viewModelScope.launch {
            try {
                homeRepository.getDailySummary(userId).collect {
                    _getDailySummary.value = it
                }
            } catch (e: Exception) {
                Log.e("Map Error", e.message.toString())
            }
        }
    }

    fun getMap() {
        viewModelScope.launch {
            try {
                homeRepository.getMap().collect {
                    _matches.value = it
                }
            } catch (e: Exception) {
                Log.e("Map Error", e.message.toString())
            }
        }
    }

    fun getMatches(
        latitude: String,
        longitude: String,
    ) {
        viewModelScope.launch {
            try {
                homeRepository.getMatches(latitude, longitude).collect {
                    _matches.value = it
                }
            } catch (e: Exception) {
                Log.e("Matches Error", e.message.toString())
            }
        }
    }

    fun postSendMatches(sendMatchDTO: SendMatchDTO) {
        viewModelScope.launch {
            try {
                homeRepository.postSendMatches(sendMatchDTO).collect {
                    _sendMatches.value = it
                }
            } catch (e: Exception) {
                Log.e("Send Matches Error", e.message.toString())
            }
        }
    }

    fun postNotifications(
    ) {
        viewModelScope.launch {
            try {
                homeRepository.postNotifications()
                    .collect {
                        _notifications.value = it
                    }
            } catch (e: Exception) {
                Log.e("Notifications Error", e.message.toString())
            }
        }
    }

    fun postNotificationRespondDTO(notificationRespondDTO: NotificationRespondDTO) {
        viewModelScope.launch {
            try {
                homeRepository.postNotificationRespondDTO(notificationRespondDTO).collect {
                    _notificationsRespond.value = it
                }
            } catch (e: Exception) {
                Log.e("Notifications Respond Error", e.message.toString())
            }
        }
    }


}