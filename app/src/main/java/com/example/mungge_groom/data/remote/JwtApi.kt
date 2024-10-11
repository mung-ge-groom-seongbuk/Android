package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.response.MatchResponseDTO
import com.example.mungge_groom.data.response.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JwtApi {
    @POST("/matching-request")
    suspend fun postMatchingRequest(
        @Body matchingRequestDTO: MatchingRequestDTO
    ): MatchResponseDTO
}