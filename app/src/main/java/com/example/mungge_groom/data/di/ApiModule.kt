package com.example.mungge_groom.data.di

import com.example.mungge_groom.data.remote.AccountApi
import com.example.mungge_groom.data.remote.ChatApi
import com.example.mungge_groom.data.remote.HomeApi
import com.example.mungge_groom.data.remote.JwtApi
import com.example.mungge_groom.data.remote.MypageApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideAccountApi(
        @NetworkModule.MainRetrofit retrofit: Retrofit
    ) : AccountApi = retrofit.create(AccountApi::class.java)

    @Provides
    @Singleton
    fun provideChatApi(
        @NetworkModule.MainRetrofit retrofit: Retrofit
    ) : ChatApi = retrofit.create(ChatApi::class.java)

    @Provides
    @Singleton
    fun provideHomeApi(
        @NetworkModule.MainRetrofit retrofit: Retrofit
    ) : HomeApi = retrofit.create(HomeApi::class.java)

    @Provides
    @Singleton
    fun provideMypageApi(
        @NetworkModule.MainRetrofit retrofit: Retrofit
    ) : MypageApi = retrofit.create(MypageApi::class.java)
    @Provides
    @Singleton
    fun provideJwtApi(
        @NetworkModule.MainRetrofit retrofit: Retrofit
    ) : JwtApi = retrofit.create(JwtApi::class.java)

}