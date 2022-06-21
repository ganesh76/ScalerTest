package com.ganeshgundu.scalertest.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.ganeshgundu.scalertest.R
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.databinding.VideoListViewItemBinding


class VideosAdapter :
    ListAdapter<VideoResult, VideosAdapter.VideosViewHolder>(DiffCallback) {

    private lateinit var itemClickListener: OnItemClickListener
    var selectedPos: Int = -1

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class VideosViewHolder(private var binding: VideoListViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: VideoResult,
            itemClickListener: OnItemClickListener,
            position: Int,
            selectedIndex: Int
        ) {
            binding.movieTitleTextView.text = data.title
            binding.movieOverview.text = data.description
            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
            Glide.with(binding.root.context)
                .load(data.thumb)
                .dontAnimate()
                .error(R.drawable.ic_broken_image)
                .placeholder(R.drawable.loading_animation)
                .apply(requestOptions)
                .into(binding.movieImageView)
            if (position == selectedIndex) {
                binding.statusTextView.visibility = View.VISIBLE
            } else {
                binding.statusTextView.visibility = View.INVISIBLE
            }
            binding.containerCardView.setOnClickListener {
                itemClickListener.onItemClick(position, data)
            }

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<VideoResult>() {
        override fun areItemsTheSame(oldItem: VideoResult, newItem: VideoResult): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: VideoResult, newItem: VideoResult): Boolean {
            return oldItem.title == newItem.title
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideosViewHolder {
        return VideosViewHolder(
            VideoListViewItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }


    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, itemClickListener, position, selectedPos)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateList(activeIndex: Int) {
        selectedPos = activeIndex
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, videoResult: VideoResult)
    }
}
