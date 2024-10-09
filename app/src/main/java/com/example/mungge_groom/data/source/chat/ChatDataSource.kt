package com.example.mungge_groom.data.source.chat

import com.example.mungge_groom.data.remote.ChatApi
import javax.inject.Inject

class ChatDataSource @Inject constructor(
    private val chatApi: ChatApi
) {

}