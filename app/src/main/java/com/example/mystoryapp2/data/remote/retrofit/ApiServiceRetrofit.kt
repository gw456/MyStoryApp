package com.example.mystoryapp2.data.remote.retrofit

import com.example.mystoryapp2.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceRetrofit {
    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") TOKEN: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): AllStoriesRetrofitResponse

    @GET("stories?location=1")
    fun getStoriesWithLocation(
        @Header("Authorization") TOKEN: String
    ): Call<AllStoriesWithLocation>

    @FormUrlEncoded
    @POST("login")
    fun loginStory(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerStory(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignUpRetrofitResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") TOKEN: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryRetrofitResponse>
}