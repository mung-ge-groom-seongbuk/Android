package com.example.mungge_groom.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.HomeRepository
import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.NotificationRespondDTO
import com.example.mungge_groom.data.request.SendMatchDTO
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
) :ViewModel() {
    private val _matches = MutableStateFlow(BaseResponse<String>())
    val matches: StateFlow<BaseResponse<String>> = _matches

    private val _sendMatches = MutableStateFlow(BaseResponse<String>())
    val sendMatches: StateFlow<BaseResponse<String>> = _sendMatches

    private val _notifications = MutableStateFlow(BaseResponse<String>())
    val notifications: StateFlow<BaseResponse<String>> = _notifications

    private val _notificationsRespond = MutableStateFlow(BaseResponse<ResultRespond>())
    val notificationsRespond: StateFlow<BaseResponse<ResultRespond>> = _notificationsRespond

    fun getMatches(matchDTO: MatchesDTO) {
        viewModelScope.launch {
            try {
                homeRepository.getMatches(matchDTO).collect {
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

    fun postNotifications(notificationDTO: NotificationDTO) {
        viewModelScope.launch {
            try {
                homeRepository.postNotifications(notificationDTO).collect {
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