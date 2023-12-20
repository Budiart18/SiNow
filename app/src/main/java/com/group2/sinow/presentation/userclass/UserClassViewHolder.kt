package com.group2.sinow.presentation.userclass

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemListCourseProgressBinding
import com.group2.sinow.model.userclass.UserCourseData

class UserClassViewHolder(
    val binding: ItemListCourseProgressBinding,
    val itemClick: (UserCourseData) -> Unit)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UserCourseData) {
        with(item) {
            binding.ivCourseImage.load(item.course?.imageUrl) {
                crossfade(true)
            }
            binding.tvCourseCategory.text = item.course?.category?.name
            binding.tvTitleCourse.text = item.course?.name
            binding.tvCourseAuthor.text = itemView.rootView.context.getString(
                R.string.format_course_by,
                item.course?.courseBy
            )
            binding.tvCourseRate.text = item.course?.rating.toString()
            binding.tvCourseLevel.text = itemView.rootView.context.getString(
                R.string.format_course_level,
                item.course?.level.toString().replaceFirstChar {
                    it.uppercase()
                }
            )
            binding.tvCourseModules.text = itemView.rootView.context.getString(
                R.string.format_course_module,
                item.course?.totalModule
            )
            binding.tvCourseDuration.text = itemView.rootView.context.getString(
                R.string.format_course_duration,
                item.course?.totalDuration
            )
            binding.tvProgress.text = itemView.rootView.context.getString(
                R.string.format_course_progress,
                item.progressPercentage
            )
            binding.progressBar.progress = item.progressPercentage ?: 0
            itemView.setOnClickListener {
                itemClick(this)
            }

        }
    }
}

