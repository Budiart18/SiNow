package com.group2.sinow.presentation.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.ItemGridCategoriesBinding
import com.group2.sinow.model.category.Category
import com.group2.sinow.presentation.homepage.viewholder.ItemGridCategoryViewHolder

class CategoryAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<ItemGridCategoryViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Category>() {
                override fun areItemsTheSame(
                    oldItem: Category,
                    newItem: Category
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Category,
                    newItem: Category
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGridCategoryViewHolder {
        val binding = ItemGridCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemGridCategoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemGridCategoryViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }
}
