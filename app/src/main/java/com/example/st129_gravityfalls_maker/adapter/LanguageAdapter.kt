package com.example.st129_gravityfalls_maker.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.st129_gravityfalls_maker.R
import com.example.st129_gravityfalls_maker.databinding.ItemLanguageBinding
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundButtonSolidBlue
import com.example.st129_gravityfalls_maker.extionsion.setBackgroundButtonSolidBlueCadet
import com.example.st129_gravityfalls_maker.model.LanguageModel

class LanguageAdapter(val context: Context) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {
    private val languageList = ArrayList<LanguageModel>()
    var onItemClick: ((LanguageModel) -> Unit)? = null
    private var currentActive = 0
    inner class LanguageViewHolder(private val binding: ItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LanguageModel, position: Int) {
            binding.apply {
                imvFlag.setImageResource(item.flag)
                txtLang.text = item.name
                if (item.activate) {
                    itemLang.background = ContextCompat.getDrawable(context, R.drawable.bg_12_stroke_white_solid_green)
                    rdbLang.setImageResource(R.drawable.ic_tick_lang)
//                    txtLang.setTextColor(ContextCompat.getColor(context, R.color.white))
//                    txtLang.typeface = ResourcesCompat.getFont(context, R.font.roboto_medium)

                } else {
                    itemLang.background = ContextCompat.getDrawable(context, R.drawable.bg_12_stroke_gray_solid_opacity)
                    rdbLang.setImageResource(R.drawable.ic_not_tick_lang)
//                    txtLang.setTextColor(ContextCompat.getColor(context, R.color.black))
//                    txtLang.typeface = ResourcesCompat.getFont(context, R.font.roboto_regular)
                }

                itemLang.setOnClickListener {
                    languageList[currentActive].activate = false
                    notifyItemChanged(currentActive)
                    currentActive = position
                    languageList[currentActive].activate = true
                    notifyItemChanged(currentActive)
                    onItemClick?.invoke(item)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return languageList.size
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = languageList[position]
        holder.bind(item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<LanguageModel>) {
        languageList.clear()
        languageList.addAll(list)
        notifyDataSetChanged()
    }

    fun submitItem(code: String) {
        languageList.forEach {
            it.activate = it.code == code
        }
    }
}