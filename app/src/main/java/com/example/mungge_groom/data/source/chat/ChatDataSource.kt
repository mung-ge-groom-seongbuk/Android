package com.example.mungge_groom.data.source.chat

import android.util.Log
import com.example.mungge_groom.data.remote.ChatApi
import com.example.mungge_groom.data.request.NotificationDTO
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.response.ChatRoomData
import com.example.mungge_groom.data.response.ChatRoomResponse
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatApi: ChatApi
) {
    fun postSendChat(sendChatDTO: SendChatDTO): Flow<ChatRoomResponse> = flow {
        val result = chatApi.postSendChat(sendChatDTO)
        emit(result)
    }.catch {
        Log.e("postSendChat Failure", it.message.toString())
    }

    fun getChatMessage(senderId: String, receiverId: String): Flow<ChatRoomData> = flow {
        val result = chatApi.getChatMessage(senderId, receiverId)
        emit(result)
    }.catch {
        Log.e("postSendChat Failure", it.message.toString())
    }
}