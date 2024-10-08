package com.example.mungge_groom.extention

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore 생성
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveUserId(userId: String) {
        dataStore.edit {
            it[USER_ID] = userId
        }
    }

    fun getUserId(): Flow<String?> = dataStore.data.map {
        it[USER_ID]
    }

    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
    }

}