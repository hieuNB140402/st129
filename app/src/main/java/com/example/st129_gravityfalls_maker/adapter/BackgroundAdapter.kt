package com.example.st129_gravityfalls_maker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.st129_gravityfalls_maker.databinding.ItemBackgroundBinding
import com.example.st129_gravityfalls_maker.extionsion.loadImageGlide
import com.example.st129_gravityfalls_maker.extionsion.setOnSingleClick

class BackgroundAdapter (val context: Context) : RecyclerView.Adapter<BackgroundAdapter.ViewHolder>(){
    private val backgroundList = ArrayList<String>()
    var itemClick : ((Int) -> Unit)? = null
    inner class ViewHolder(val binding: ItemBackgroundBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: String, position: Int){
            loadImageGlide(binding.root, item, binding.imvImage)
            binding.root.setOnSingleClick { itemClick?.invoke(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return backgroundList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = backgroundList[position]
        holder.bind(item, position)
    }
    fun submitList(list: ArrayList<String>){
        backgroundList.clear()
        backgroundList.addAll(list)
        notifyDataSetChanged()
    }
}