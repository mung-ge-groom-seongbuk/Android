package com.example.mungge_groom.data.source.jwt

import com.example.mungge_groom.data.remote.JwtApi
import com.example.mungge_groom.data.repository.JwpRepository
import com.example.mungge_groom.data.repository.MypageRepository
import com.example.mungge_groom.data.request.MatchingRequestDTO
import com.example.mungge_groom.data.response.MatchResponseDTO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JwtRepositoroyImpl @Inject constructor(
    private val jwtDataSource: JwtDataSource
) : JwpRepository {
    override suspend fun postMatchingRequest(matchingRequestDTO: MatchingRequestDTO): Flow<MatchResponseDTO> = jwtDataSource.postMatchRequest(matchingRequestDTO)
}