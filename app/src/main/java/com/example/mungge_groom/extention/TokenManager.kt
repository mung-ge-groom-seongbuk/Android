package com.example.mungge_groom.extention

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.mungge_groom.data.response.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

// DataStore 생성
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_store")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    fun saveToken(token: String) = runBlocking {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun deleteAccessToken() {
        dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN_KEY)
        }
    }
    fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN_KEY]
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit {
            it[USER_ID] = userId
        }
    }

    suspend fun saveUserdi(d: String) {
        dataStore.edit {
            it[di] = d
        }
    }
    suspend fun saveUserdu(a: String) {
        dataStore.edit {
            it[du] = a
        }
    }
    suspend fun saveUserpa(c: String) {
        dataStore.edit {
            it[pa] = c
        }
    }
    suspend fun saveUserca(g: String) {
        dataStore.edit {
            it[ca] = g
        }
    }

    fun getUserId(): Flow<String?> = dataStore.data.map {
        it[USER_ID]
    }

    fun getUserd(): Flow<String?> = dataStore.data.map {
        it[di]
    }

    fun getUserdd(): Flow<String?> = dataStore.data.map {
        it[du]
    }

    fun getUserddd(): Flow<String?> = dataStore.data.map {
        it[pa]
    }

    fun getUserdddd(): Flow<String?> = dataStore.data.map {
        it[ca]
    }

    suspend fun saveFireBaseTokenId(userId: String) {
        dataStore.edit {
            it[FIREBASE_TOKEN] = userId
        }
    }

    fun getFireBaseTokenIdId(): Flow<String?> = dataStore.data.map {
        it[FIREBASE_TOKEN]
    }

    suspend fun saveUser(user: User, context: Context) {
        val gson = Gson()
        val userJson = gson.toJson(user) // User 객체를 JSON 문자열로 변환
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = userJson // JSON 문자열을 DataStore에 저장
        }
    }

    fun getUser(context: Context): Flow<User?> {
        val gson = Gson()
        return context.dataStore.data.map { preferences ->
            val userJson = preferences[USER_KEY] // 저장된 JSON 문자열을 가져옴
            if (userJson != null) {
                gson.fromJson(userJson, User::class.java) // JSON 문자열을 User 객체로 변환
            } else {
                null // 저장된 데이터가 없으면 null 반환
            }
        }
    }
    companion object {
        private val USER_ID = stringPreferencesKey("user_id")
        private val USER_KEY = stringPreferencesKey("user")
        private val FIREBASE_TOKEN = stringPreferencesKey("firebase_token")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

        private val di = stringPreferencesKey("di")
        private val du = stringPreferencesKey("du =")
        private val pa = stringPreferencesKey("pa")
        private val ca = stringPreferencesKey("ca")

    }

}