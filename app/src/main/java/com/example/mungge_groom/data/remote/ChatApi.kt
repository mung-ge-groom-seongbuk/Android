package com.example.mungge_groom.data.remote

import com.example.mungge_groom.data.request.SendChatDTO
import com.example.mungge_groom.data.response.ChatRoomData
import com.example.mungge_groom.data.response.ChatRoomResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {
    @POST("/chat/send")
    suspend fun postSendChat(
        @Body sendChatDTO: SendChatDTO
    ): ChatRoomResponse

    @GET("/chat/messages/{sender_id}/{receiver_id}")
    suspend fun getChatMessage(
        @Path("sender_id") senderId: String,
        @Path("receiver_id") receiverId: String,
    ): ChatRoomData

}