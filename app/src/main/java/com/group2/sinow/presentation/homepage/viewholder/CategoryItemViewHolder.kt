package com.group2.sinow.presentation.homepage.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.databinding.ItemCategoryTextBinding
import com.group2.sinow.databinding.ItemGridCategoriesBinding
import com.group2.sinow.model.category.Category

class ItemGridCategoryViewHolder(
    private val binding: ItemGridCategoriesBinding,
    val itemClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        with(item) {
            binding.imageCategories.load(item.categoryImage)
            binding.tvCategoriesName.text = item.categoryName
            itemView.setOnClickListener {
                itemClick(this)
            }
        }
    }

}

class ItemTextCategoryViewHolder(
    private val binding: ItemCategoryTextBinding,
    val itemClick: (Category) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        with(item) {
            binding.tvCategoryName.text = item.categoryName
            itemView.setOnClickListener {
                itemClick(this)
            }
        }
    }

}