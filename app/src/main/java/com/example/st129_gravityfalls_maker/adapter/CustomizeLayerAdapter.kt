package com.example.st129_gravityfalls_maker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.databinding.ItemCustomizeBinding
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.pxToDpInt
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.model.ItemNavCustomModel
import com.example.st129_gravityfalls_maker.utils.KeyApp
import com.example.st129_gravityfalls_maker.utils.SystemUtils.shimmer
import com.facebook.shimmer.ShimmerDrawable


class CustomizeLayerAdapter(val context: Context) : RecyclerView.Adapter<CustomizeLayerAdapter.CustomizeLayerViewHolder>() {
    private val itemList: ArrayList<ItemNavCustomModel> = arrayListOf()
    var onItemClick: ((ItemNavCustomModel, Int) -> Unit)? = null
    var onNoneClick: ((Int) -> Unit)? = null
    var onRandomClick: (() -> Unit)? = null

    inner class CustomizeLayerViewHolder(val binding: ItemCustomizeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemNavCustomModel, position: Int) {
            binding.apply {
                val shimmerDrawable = ShimmerDrawable().apply {
                    setShimmer(shimmer)
                }

                val isBody = item.path.contains("/${KeyApp.NameFolderLayer.BODY}/")
                when (position) {
                    0 -> {
                        if (isBody) {
                            btnNone.gone()
                            imvImage.gone()
                            btnRandom.show()
                        } else {
                            btnNone.show()
                            imvImage.gone()
                            btnRandom.gone()
                        }
                    }

                    1 -> {
                        if (!isBody) {
                            btnNone.gone()
                            imvImage.gone()
                            btnRandom.show()
                        } else {
                            btnNone.gone()
                            imvImage.show()
                            btnRandom.gone()
                            Glide.with(root).load(item.path).placeholder(shimmerDrawable).error(shimmerDrawable).into(imvImage)
                        }
                    }

                    else -> {
                        btnNone.gone()
                        imvImage.show()
                        btnRandom.gone()
                        Glide.with(root).load(item.path).placeholder(shimmerDrawable).error(shimmerDrawable).into(imvImage)
                    }
                }

                if (item.isSelected) {
                    binding.viewFocus.show()
                    binding.cvParent.strokeColor = Color.parseColor("#253101")
                    binding.cvParent.strokeWidth = pxToDpInt(context, 1)
                } else {
                    binding.viewFocus.gone()
                    binding.cvParent.strokeWidth = 0
                }

                binding.imvImage.setOnClickListener {
                    onItemClick?.invoke(item, position)
                }

                binding.btnRandom.setOnClickListener {
                    onRandomClick?.invoke()
                }

                binding.btnNone.setOnClickListener {
                    onNoneClick?.invoke(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomizeLayerViewHolder {
        return CustomizeLayerViewHolder(ItemCustomizeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CustomizeLayerViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ItemNavCustomModel>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }
}