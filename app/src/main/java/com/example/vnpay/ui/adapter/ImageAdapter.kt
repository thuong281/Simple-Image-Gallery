package com.example.vnpay.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vnpay.data.model.Image
import com.example.vnpay.databinding.ImageItemBinding
import com.example.vnpay.utils.Constants
import java.util.concurrent.Executors

typealias OnPictureSelected = (Int) -> Unit

class ImageAdapter(private val context: Context): ListAdapter<Image, ImageAdapter.ImageViewHolder>(
    AsyncDifferConfig.Builder(ImageDiffCallback())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    var onPictureSelected: OnPictureSelected = {}

    class ImageDiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.imageName == newItem.imageName
        }
    }

    class ImageViewHolder(private val context: Context, private val binding: ImageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageItem: Image, onPictureSelected: OnPictureSelected) {
            Glide.with(context)
                .load(imageItem.imagePath)
                .dontAnimate()
                .dontTransform()
                .apply(RequestOptions().centerCrop().override(Constants.LIST_IMAGE_SIZE))
                .into(binding.image)
            binding.root.setOnClickListener {
                onPictureSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position)) { picturePosition ->
            onPictureSelected(picturePosition)
        }
    }
}