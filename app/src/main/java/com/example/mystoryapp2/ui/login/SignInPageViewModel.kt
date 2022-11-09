package com.example.mystoryapp2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp2.data.local.StatusDataClass
import com.example.mystoryapp2.data.remote.DataStoryRepository
import kotlinx.coroutines.launch

class SignInPageViewModel(private val pref: DataStoryRepository) : ViewModel() {
    fun saveStatus(statusDataClass: StatusDataClass) {
        viewModelScope.launch {
            pref.saveStatus(statusDataClass)
        }
    }
}