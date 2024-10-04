package com.example.mungge_groom.data.di

import com.example.mungge_groom.data.remote.AccountApi
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
}