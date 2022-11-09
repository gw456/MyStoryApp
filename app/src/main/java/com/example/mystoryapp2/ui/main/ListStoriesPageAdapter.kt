package com.example.mystoryapp2.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp2.data.local.StoryDataClass
import com.example.mystoryapp2.data.local.StoryItem
import com.example.mystoryapp2.databinding.ItemUserRowBinding
import com.example.mystoryapp2.ui.detail.DetailPageActivity

class ListStoriesPageAdapter :
    PagingDataAdapter<StoryItem, ListStoriesPageAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(
                StoryDataClass(
                    data.name,
                    data.photoUrl,
                    data.createdAt,
                    data.description,
                    0.0,
                    0.0
                )
            )
        }
    }

    class MyViewHolder(private val binding: ItemUserRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StoryDataClass) {
            binding.tvStoryAccount.text = data.name
            Glide.with(itemView.context)
                .load(data.photoUrl)
                .into(binding.imgStoryPhoto)
            binding.tvDateStory.text = data.time
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailPageActivity::class.java)
                intent.putExtra(DetailPageActivity.DATA, data)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStoryPhoto, "profile"),
                        Pair(binding.tvStoryAccount, "name"),
                        Pair(binding.tvDateStory, "date")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}