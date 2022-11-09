package com.example.mystoryapp2.helper

import android.content.Context
import com.example.mystoryapp2.data.remote.paging.StoryRepository
import com.example.mystoryapp2.data.remote.retrofit.ApiConfigRetrofit

object InjectionHelperClass {
    fun provideRepository(context: Context, token: String): StoryRepository {
        val apiService = ApiConfigRetrofit.getApiService()
        return StoryRepository(apiService, token)
    }
}