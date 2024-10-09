package com.example.mungge_groom.data.source.chat

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.source.account.AccountApiDataSource
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {
    override suspend fun postSendChat(sendChatDTO: SendChatDTO): Flow<BaseResponse<String>> =
        chatDataSource.postSendChat(sendChatDTO)

    override suspend fun getChatMessage(
        senderId: String,
        receiverId: String
    ): Flow<BaseResponse<String>> = chatDataSource.getChatMessage(senderId,receiverId)

}
