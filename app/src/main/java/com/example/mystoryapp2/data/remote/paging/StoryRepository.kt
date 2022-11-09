package com.example.mystoryapp2.data.remote.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp2.data.local.StoryItem
import com.example.mystoryapp2.data.remote.retrofit.ApiServiceRetrofit

class StoryRepository(private val apiServiceRetrofit: ApiServiceRetrofit, private val token: String) {
    fun getStory(): LiveData<PagingData<StoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiServiceRetrofit, token)
            }
        ).liveData
    }
}