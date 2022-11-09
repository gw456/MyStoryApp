package com.example.mystoryapp2.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class StoryDataClass(
    val name: String,
    val photoUrl: String,
    val time: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
): Parcelable
