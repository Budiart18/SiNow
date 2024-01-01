package com.group2.sinow.presentation.course.filtercourse

import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.ItemCheckboxFilterBinding
import com.group2.sinow.model.category.Category

class CategoryFilterViewHolder(
    private val binding: ItemCheckboxFilterBinding,
    private val listener: CategoryItemListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Category) {
        with(item) {
            binding.cbItemCategory.text = item.categoryName
            binding.cbItemCategory.isChecked = listener.getSelectedCategories()?.contains(item) == true
            binding.cbItemCategory.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listener.onCategoryChecked(item)
                } else {
                    listener.onCategoryUnchecked(item)
                }
            }
        }
    }
}
