package com.example.mungge_groom.data.source.mypage

import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.repository.MypageRepository
import com.example.mungge_groom.data.source.chat.ChatDataSource
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MypageRepository {
    
}