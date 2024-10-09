package com.example.mungge_groom.data.di

import com.example.mungge_groom.data.repository.AccountRepository
import com.example.mungge_groom.data.source.account.AccountApiDataSource
import com.example.mungge_groom.data.source.account.AccountApiRepositoryImpl
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

}