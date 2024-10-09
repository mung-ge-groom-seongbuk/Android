package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.LogInDTO
import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.request.SignUpDTO
import com.example.mungge_groom.data.request.UpdateProfileDTO
import com.example.mungge_groom.ui.base.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChatApi {
    @POST("/chat/send")
    suspend fun postSendChat(
        @Body sendChatDTO: SendChatDTO
    ): BaseResponse<String>

    @GET("/chat/messages/{sender_id}/{receiver_id}")
    suspend fun getChatMessage(
        @Path("sender_id") senderId: String,
        @Path("receiver_id") receiverId: String,
    ) :BaseResponse<String>

}