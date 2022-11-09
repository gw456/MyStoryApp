package com.example.mystoryapp2.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp2.data.local.StoryDataClass
import com.example.mystoryapp2.databinding.ItemUserRowBinding
import com.example.mystoryapp2.ui.detail.DetailPageActivity

class StoriesPageAdapter(private val listStories: List<StoryDataClass>) : RecyclerView.Adapter<StoriesPageAdapter.ListViewHolder>() {
    class ListViewHolder(var binding: ItemUserRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder =
        ListViewHolder(ItemUserRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))

    override fun onBindViewHolder(viewHolder: ListViewHolder, position: Int) {
        val (name, photoUrl, time) = listStories[position]
        viewHolder.binding.tvStoryAccount.text = name
        Glide.with(viewHolder.itemView.context)
            .load(photoUrl)
            .into(viewHolder.binding.imgStoryPhoto)
        viewHolder.binding.tvDateStory.text = time
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, DetailPageActivity::class.java)
            intent.putExtra(DetailPageActivity.DATA, listStories[position])
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    viewHolder.itemView.context as Activity,
                    Pair(viewHolder.binding.imgStoryPhoto, "profile"),
                    Pair(viewHolder.binding.tvStoryAccount, "name"),
                    Pair(viewHolder.binding.tvDateStory, "date")
                )
            viewHolder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int = listStories.size
}