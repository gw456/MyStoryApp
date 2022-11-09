package com.example.mystoryapp2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp2.data.remote.DataStoryRepository
import com.example.mystoryapp2.ui.login.SignInPageViewModel
import com.example.mystoryapp2.ui.main.MainStoryPageViewModel

class StoryViewModelFactory(private val pref: DataStoryRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainStoryPageViewModel::class.java) -> {
                MainStoryPageViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignInPageViewModel::class.java) -> {
                SignInPageViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}