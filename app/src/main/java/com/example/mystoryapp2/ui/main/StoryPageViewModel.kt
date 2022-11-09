package com.example.mystoryapp2.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp2.data.remote.paging.StoryRepository
import com.example.mystoryapp2.data.local.StoryItem
import com.example.mystoryapp2.helper.InjectionHelperClass

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {
    val story: LiveData<PagingData<StoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)
}

class StoryViewModelFactory(private val context: Context, private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(InjectionHelperClass.provideRepository(context, token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}