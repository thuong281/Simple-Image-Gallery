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
import com.example.vnpay.R
import com.example.vnpay.data.model.ImageFolder
import com.example.vnpay.databinding.FolderItemBinding
import java.util.concurrent.Executors

typealias OnFolderSelected = (Int) -> Unit

class FolderAdapter(private val context: Context): ListAdapter<ImageFolder, FolderAdapter.FolderViewHolder>(
    AsyncDifferConfig.Builder(FolderDiffCallback())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    var onFolderSelected: OnFolderSelected = {}

    class FolderDiffCallback : DiffUtil.ItemCallback<ImageFolder>() {
        override fun areItemsTheSame(oldItem: ImageFolder, newItem: ImageFolder): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: ImageFolder, newItem: ImageFolder): Boolean {
            return oldItem.folderName == newItem.folderName && oldItem.getNumberOfImages() == newItem.getNumberOfImages()
        }
    }

    class FolderViewHolder(private val context: Context, private val binding: FolderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folderItem: ImageFolder, onFolderSelected: OnFolderSelected) {
            Glide.with(context)
                .load(folderItem.firstImage)
                .apply(RequestOptions().centerCrop())
                .into(binding.firstImage)
            binding.imageCount.text =
                context.getString(R.string.images_count, folderItem.getNumberOfImages())
            binding.folderName.text = folderItem.folderName

            binding.root.setOnClickListener {
                onFolderSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = FolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        holder.bind(getItem(position)) { folderPosition ->
            onFolderSelected(folderPosition)
        }
    }
}