package com.example.mungge_groom.data.repository

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.ui.base.BaseResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

interface ChatRepository {

    suspend fun postSendChat(sendChatDTO: SendChatDTO) : Flow<BaseResponse<String>>
    suspend fun getChatMessage(senderId : String, receiverId : String) : Flow<BaseResponse<String>>

}
