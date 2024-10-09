package com.example.mungge_groom.data.source.mypage

import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.repository.MypageRepository
import com.example.mungge_groom.data.response.UserResponse
import com.example.mungge_groom.data.source.chat.ChatDataSource
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MypageRepository {
    override suspend fun getUsers(): Flow<BaseResponse<List<UserResponse>>>
    = mypageDataSource.getUsers()

}