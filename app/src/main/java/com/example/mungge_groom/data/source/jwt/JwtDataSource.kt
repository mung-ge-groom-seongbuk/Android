package com.example.mungge_groom.data.source.jwt

import android.util.Log
import com.example.mungge_groom.data.remote.JwtApi
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.response.MatchResponseDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class JwtDataSource @Inject constructor(
    private val jwpApi: JwtApi
) {
    fun postMatchRequest(matchingRequestDTO : MatchingRequestDTO) : Flow<MatchResponseDTO> =
        flow {
            val result = jwpApi.postMatchingRequest(matchingRequestDTO)
            emit(result)
        }.catch {
            Log.e("postRunning Failure", it.message.toString())
        }
}