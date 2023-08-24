package com.example.rawggamebase.features.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rawggamebase.R
import com.example.rawggamebase.databinding.ItemGameBinding
import com.example.rawggamebase.databinding.ItemViewMoreBinding
import com.example.rawggamebase.features.model.GameModel

class GameListAdapter(
    val onItemClick: (GameModel) -> Unit,
    var onViewMore: (() -> Unit)? = null
) : ListAdapter<GameModel, GameListAdapter.GameViewHolder>(GameModelDiffUtil()) {

    companion object {
        const val VIEW_MORE = 10
    }

    class GameModelDiffUtil : DiffUtil.ItemCallback<GameModel>() {
        override fun areItemsTheSame(oldItem: GameModel, newItem: GameModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GameModel, newItem: GameModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return when (viewType) {
            VIEW_MORE -> {
                ViewMoreHolder(
                    ItemViewMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> {
                ItemGameHolder(
                    ItemGameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return currentList[position].viewType
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        (holder as? ItemGameHolder)?.bindItem(currentList[position])
    }

    abstract class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ItemGameHolder(private val binding: ItemGameBinding) :
        GameViewHolder(binding.root) {
        fun bindItem(item: GameModel) {
            Glide.with(itemView.context)
                .load(item.imageLink)
                .into(binding.ivCoverImage)

            with(binding) {
                tvTitle.text = item.title
                tvReleaseDate.text =
                    root.context.getString(R.string.release_date_template, item.releaseDate)
                tvRating.text = item.rating
            }

            binding.root.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }

    inner class ViewMoreHolder(binding: ItemViewMoreBinding) : GameViewHolder(binding.root) {
        init {
            binding.tvRating.setOnClickListener {
                onViewMore?.invoke()
            }
        }
    }

}