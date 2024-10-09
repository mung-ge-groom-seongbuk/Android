package com.example.mungge_groom.data.source.home

import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.repository.HomeRepository
import com.example.mungge_groom.data.source.chat.ChatDataSource
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeDataSource: HomeDataSource
) : HomeRepository {
}