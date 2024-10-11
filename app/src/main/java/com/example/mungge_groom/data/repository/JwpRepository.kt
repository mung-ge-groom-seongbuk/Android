package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.MatchesDTO
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.data.response.MatchResponseDTO
import com.example.mungge_groom.data.response.User
import com.example.mungge_groom.data.response.UserResponse
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface JwpRepository {
    suspend fun postMatchingRequest(matchingRequestDTO: MatchingRequestDTO) : Flow<MatchResponseDTO>


}
