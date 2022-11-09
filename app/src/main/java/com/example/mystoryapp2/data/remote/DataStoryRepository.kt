package com.example.mystoryapp2.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mystoryapp2.data.local.StatusDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoryRepository private constructor(
    private val dataStore: DataStore<Preferences>
    ){

    suspend fun saveStatus(statusDataClass: StatusDataClass) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = statusDataClass.token
            preferences[STATE_KEY] = statusDataClass.isLogin
        }
    }

    fun getStatus() : Flow<StatusDataClass> {
        return dataStore.data.map { preferences ->
            StatusDataClass(
                preferences[TOKEN] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: DataStoryRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): DataStoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = DataStoryRepository(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}