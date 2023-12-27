package com.group2.sinow.presentation.course.filtercourse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.ItemCheckboxFilterBinding
import com.group2.sinow.model.category.Category

class CategoryFilterAdapter(private val listener: CategoryItemListener) :
    RecyclerView.Adapter<CategoryFilterViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterViewHolder {
        val binding =
            ItemCheckboxFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryFilterViewHolder(binding,listener)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: CategoryFilterViewHolder, position: Int) {
        val category = dataDiffer.currentList[position]

        holder.bind(category)
    }

    fun submitData(data: List<Category>) {
        dataDiffer.submitList(data)
    }

}

interface CategoryItemListener {
    fun onCategoryChecked(category: Category)
    fun onCategoryUnchecked(category: Category)

    fun getSelectedCategories() : List<Category>?
}