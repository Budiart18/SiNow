package com.group2.sinow.presentation.homepage.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemGridCourseBinding
import com.group2.sinow.model.course.Course
import com.group2.sinow.utils.toCurrencyFormat

class ItemGridCourseViewHolder(
    private val binding: ItemGridCourseBinding,
    val itemClick: (Course) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Course) {
        with(item) {
            binding.ivCourseImage.load(item.imageUrl) {
                crossfade(true)
            }
            binding.tvCourseCategory.text = item.category?.name
            binding.tvCourseRate.text = item.rating.toString()
            binding.tvCourseName.text = item.name
            binding.tvCourseAuthor.text = itemView.rootView.context.getString(
                R.string.format_course_by,
                item.courseBy
            )
            binding.tvCourseLevel.text = item.level?.replaceFirstChar{
                it.uppercase()
            }
            binding.tvCourseDuration.text = itemView.rootView.context.getString(
                R.string.format_course_duration,
                item.totalDuration
            )
            binding.tvCourseModules.text = itemView.rootView.context.getString(
                R.string.format_course_module,
                item.totalModule
            )
            binding.btnBuy.text = itemView.rootView.context.getString(
                R.string.format_btn_buy,
                item.price?.toDouble()?.toCurrencyFormat()
            )
            itemView.setOnClickListener {
                itemClick(this)
            }
        }
    }

}