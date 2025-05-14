package com.example.st129_gravityfalls_maker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.databinding.ItemMyAlbumBinding
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundSolidTransparent
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.model.MyAlbumModel
import com.example.st129_gravityfalls_maker.utils.SystemUtils.shimmer
import com.facebook.shimmer.ShimmerDrawable


class MyAlbumAdapter(val context: Context) :
    RecyclerView.Adapter<MyAlbumAdapter.MyLibraryViewHolder>() {
    private var myAlbumList: ArrayList<MyAlbumModel> = arrayListOf()
    var onItemClick: ((String) -> Unit)? = null
    var onMoreClick: ((String, Int, View) -> Unit)? = null
    var onLongClick: ((Int) -> Unit)? = null
    var onItemTick: ((Int) -> Unit)? = null

    inner class MyLibraryViewHolder(val binding: ItemMyAlbumBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MyAlbumModel, position: Int) {

            val shimmerDrawable = ShimmerDrawable().apply {
                setShimmer(shimmer)
            }

            Glide.with(binding.root).load(item.path).placeholder(shimmerDrawable)
                .error(shimmerDrawable).into(binding.imvImage)

            if (item.isShowSelection) {
                binding.btnSelect.show()
                binding.btnMore.gone()
            } else {
                binding.btnSelect.gone()
                binding.btnMore.show()
            }

            if (item.isSelected) {
                binding.btnSelect.setImageResource(R.drawable.ic_select)
            } else {
                binding.btnSelect.setImageResource(R.drawable.ic_not_select)
            }

            binding.root.setOnSingleClick {
                onItemClick?.invoke(item.path)
            }
            binding.btnMore.setOnSingleClick {
                onMoreClick?.invoke(item.path, position, it)
            }
            binding.root.setOnLongClickListener {
                onLongClick?.invoke(position)
                return@setOnLongClickListener true
            }
            binding.btnSelect.setOnSingleClick {
                onItemTick?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyLibraryViewHolder {
        return MyLibraryViewHolder(
            ItemMyAlbumBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return myAlbumList.size
    }

    override fun onBindViewHolder(holder: MyLibraryViewHolder, position: Int) {
        val item = myAlbumList[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<MyAlbumModel>) {
        myAlbumList.clear()
        myAlbumList.addAll(list)
        notifyDataSetChanged()
    }

    fun submitItem(position: Int, isSelect: Boolean) {
        myAlbumList[position].isSelected = isSelect
        notifyItemChanged(position)
    }

}