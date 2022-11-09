package com.example.mystoryapp2.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddStoryRetrofitResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
