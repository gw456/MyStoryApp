package com.example.mystoryapp2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystoryapp2.data.local.StoryDataClass
import com.example.mystoryapp2.databinding.ActivityDetailBinding

class DetailPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent?.getParcelableExtra<StoryDataClass>(DATA)
        if (data != null) {
            binding.apply {
                Glide.with(this@DetailPageActivity)
                    .load(data.photoUrl)
                    .into(profileImageView)
                nameTextView.text = data.name
                tvDateStory.text = data.time
                descTextView.text = data.description
            }
        }
    }

    companion object {
        const val DATA = "data"
    }
}