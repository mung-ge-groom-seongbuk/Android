package com.example.mungge_groom.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mungge_groom.data.repository.JwpRepository
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.response.MatchResponseDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class JwtViewModel @Inject constructor(
    private val jwpRepository: JwpRepository
) : ViewModel(){
    private val _matchRequestData = MutableStateFlow(MatchResponseDTO())
    val matchRequestData: StateFlow<MatchResponseDTO> = _matchRequestData
    fun postMatchingRequestDTO(matchingRequestDTO: MatchingRequestDTO) {
        viewModelScope.launch {
            try {
                jwpRepository.postMatchingRequest(matchingRequestDTO).collect {
                    _matchRequestData.value = it
                }
            } catch (e: Exception) {
                Log.e("Notifications Respond Error", e.message.toString())
            }
        }
    }
}