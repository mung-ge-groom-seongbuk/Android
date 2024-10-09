package com.example.mungge_groom.data.source.chat

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.source.account.AccountApiDataSource
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDataSource: ChatDataSource
) : ChatRepository {
}