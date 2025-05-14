package com.example.st129_gravityfalls_maker.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.st129_gravityfalls_maker.databinding.ItemColorBinding
import com.example.st129_gravityfalls_maker.extionsion.gone
import com.example.st129_gravityfalls_maker.extionsion.pxToDpInt
import com.example.st129_gravityfalls_maker.extionsion.show
import com.example.st129_gravityfalls_maker.model.ItemColorModel


class ItemColorLayerAdapter (val context: Activity) : RecyclerView.Adapter<ItemColorLayerAdapter.ItemColorViewHolder>(){
    var itemColor: ArrayList<ItemColorModel> = arrayListOf()
    var onItemClick: ((Int) -> Unit)? = null
    inner class ItemColorViewHolder(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ItemColorModel, position: Int){
            binding.imvImage.setBackgroundColor(Color.parseColor(item.color))
            if (!item.isSelected){
                binding.cvFocus.strokeWidth = pxToDpInt(context,2)
            }else{
                binding.cvFocus.strokeWidth = 0
            }
            binding.root.setOnClickListener {
                onItemClick?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemColorViewHolder {
        return ItemColorViewHolder(ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itemColor.size
    }

    override fun onBindViewHolder(holder: ItemColorViewHolder, position: Int) {
        val item = itemColor[position]
        holder.bind(item, position)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<ItemColorModel>){
        itemColor.clear()
        itemColor.addAll(list)
        notifyDataSetChanged()
    }
}