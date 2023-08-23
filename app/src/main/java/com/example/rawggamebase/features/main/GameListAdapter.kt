package com.example.rawggamebase.features.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rawggamebase.databinding.ItemGameBinding
import com.example.rawggamebase.features.main.model.GameModel

class GameListAdapter :
    ListAdapter<GameModel, GameListAdapter.ItemGameHolder>(GameModelDiffUtil()) {

    class GameModelDiffUtil : DiffUtil.ItemCallback<GameModel>() {
        override fun areItemsTheSame(oldItem: GameModel, newItem: GameModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameModel, newItem: GameModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGameHolder {
        return ItemGameHolder(
            ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemGameHolder, position: Int) {
        holder.bindItem(currentList[position])
    }

    inner class ItemGameHolder(private val binding: ItemGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: GameModel) {
            Glide.with(itemView.context)
                .load(item.imageLink)
                .into(binding.ivCoverImage)

            with(binding) {
                tvTitle.text = item.title
                tvReleaseDate.text = item.releaseDate
                tvRating.text = item.rating
            }
        }
    }
}