package com.example.mungge_groom.data.source.chat

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.response.ChatRoomData
import com.example.mungge_groom.data.response.ChatRoomResponse
import com.example.mungge_groom.data.source.account.AccountApiDataSource
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {
    override suspend fun postSendChat(sendChatDTO: SendChatDTO): Flow<ChatRoomResponse> = chatDataSource.postSendChat(sendChatDTO)

    override suspend fun getChatMessage(
        senderId: String,
        receiverId: String
    ): Flow<ChatRoomData> = chatDataSource.getChatMessage(senderId,receiverId)

}
