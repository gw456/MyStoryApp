package com.example.mystoryapp2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp2.data.remote.DataStoryRepository
import com.example.mystoryapp2.data.local.StatusDataClass
import kotlinx.coroutines.launch

class MainStoryPageViewModel(private val pref: DataStoryRepository) : ViewModel() {
    fun getStatus(): LiveData<StatusDataClass> {
        return pref.getStatus().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}