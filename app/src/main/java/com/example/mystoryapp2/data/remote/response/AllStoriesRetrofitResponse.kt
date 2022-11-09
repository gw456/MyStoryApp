package com.example.mystoryapp2.data.remote.response

import com.example.mystoryapp2.data.local.StoryItem
import com.google.gson.annotations.SerializedName

data class AllStoriesRetrofitResponse(

    @field:SerializedName("listStory")
	val listStory: List<StoryItem> = emptyList(),

    @field:SerializedName("error")
	val error: Boolean = false,

    @field:SerializedName("message")
	val message: String = ""
)
