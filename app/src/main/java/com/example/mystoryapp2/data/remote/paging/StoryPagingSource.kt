package com.example.mystoryapp2.data.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp2.data.local.StoryItem
import com.example.mystoryapp2.data.remote.retrofit.ApiServiceRetrofit

class StoryPagingSource(private val apiServiceRetrofit: ApiServiceRetrofit, private val token: String) : PagingSource<Int, StoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        val page = params.key ?: INITIAL_PAGE_INDEX
        Log.d("Token", token)
        return try {
            val responseData = apiServiceRetrofit.getAllStories(token, page, params.loadSize)
            val data = responseData.listStory

            LoadResult.Page(
                data = data,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (data.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            Log.e("Error: ", exception.toString())
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}