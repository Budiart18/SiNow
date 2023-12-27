package com.group2.sinow.presentation.course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemListCourseBinding
import com.group2.sinow.model.course.Course

class CourseItemAdapter(
    private val itemClick: (Course) -> Unit
) : RecyclerView.Adapter<ItemListCourseViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Course>() {
                override fun areItemsTheSame(
                    oldItem: Course,
                    newItem: Course
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Course,
                    newItem: Course
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemListCourseViewHolder {
        val binding =
            ItemListCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemListCourseViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemListCourseViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<Course>) {
        dataDiffer.submitList(data)
    }


}

class ItemListCourseViewHolder(
    private val binding: ItemListCourseBinding,
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
            binding.tvBtnBuy.text = item.type?.replaceFirstChar {
                it.uppercase()
            }
            if (item.type == TYPE_PREMIUM) {
                binding.ivPremium.visibility = View.VISIBLE
            } else {
                binding.ivPremium.visibility = View.GONE
            }
            itemView.setOnClickListener {
                itemClick(item)
            }
        }
    }

    companion object {
        const val TYPE_PREMIUM = "premium"
    }

}