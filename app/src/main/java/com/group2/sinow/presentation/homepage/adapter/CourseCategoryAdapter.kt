package com.group2.sinow.presentation.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemCategoryTextBinding
import com.group2.sinow.model.category.Category
import com.group2.sinow.presentation.homepage.viewholder.ItemTextCategoryViewHolder

class CourseCategoryAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<ItemTextCategoryViewHolder>() {

    private var selectedCategory: Category? = null

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTextCategoryViewHolder {
        val binding = ItemCategoryTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTextCategoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemTextCategoryViewHolder, position: Int) {
        val category = dataDiffer.currentList[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
            itemClick(category)
        }
        holder.itemView.setBackgroundResource(
            if (category == selectedCategory) R.drawable.bg_primary_rounded else android.R.color.transparent
        )
    }

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }

    fun setSelectedCategory(selectedCategory: Category?) {
        this.selectedCategory = selectedCategory
        notifyDataSetChanged()
    }

}