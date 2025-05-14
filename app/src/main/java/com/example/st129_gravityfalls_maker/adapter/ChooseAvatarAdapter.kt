package com.example.st129_gravityfalls_maker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.databinding.ItemMyAlbumBinding
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.model.CustomizeModel
import com.example.st129_gravityfalls_maker.utils.SystemUtils
import com.facebook.shimmer.ShimmerDrawable

class ChooseAvatarAdapter(val context: Context) :
    RecyclerView.Adapter<ChooseAvatarAdapter.ViewHolder>() {
    private val avatarList = ArrayList<CustomizeModel>()
    var onItemClick: ((String, Int) -> Unit)? = null

    inner class ViewHolder(val binding: ItemMyAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CustomizeModel, position: Int) {
            binding.btnMore.gone()
            binding.btnSelect.gone()
            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(SystemUtils.shimmer)
            }
            Glide.with(binding.root).load(item.avatar).placeholder(shimmerDrawable)
                .into(binding.imvImage)
            binding.root.setOnSingleClick { onItemClick?.invoke(item.avatar, position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMyAlbumBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = avatarList[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<CustomizeModel>) {
        avatarList.clear()
        avatarList.addAll(list)
        notifyDataSetChanged()
    }
}