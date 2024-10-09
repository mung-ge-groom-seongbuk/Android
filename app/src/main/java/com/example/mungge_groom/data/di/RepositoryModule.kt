package com.example.mungge_groom.data.di

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.repository.ChatRepository
import com.example.mungge_groom.data.repository.HomeRepository
import com.example.mungge_groom.data.source.account.AccountApiDataSource
import com.example.mungge_groom.data.source.account.AccountApiRepositoryImpl
import com.example.mungge_groom.data.source.chat.ChatDataSource
import com.example.mungge_groom.data.source.chat.ChatRepositoryImpl
import com.example.mungge_groom.data.source.home.HomeDataSource
import com.example.mungge_groom.data.source.home.HomeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainApiRepository(accountApiDataSource: AccountApiDataSource): AccountRepository =
        AccountApiRepositoryImpl(accountApiDataSource)

    @Singleton
    @Provides
    fun provideHomeApiRepository(homeDataSource: HomeDataSource): HomeRepository =
        HomeRepositoryImpl(homeDataSource)

    @Singleton
    @Provides
    fun provideChatApiRepository(chatDataSource: ChatDataSource): ChatRepository =
        ChatRepositoryImpl(chatDataSource)

}