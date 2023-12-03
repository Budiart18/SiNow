package com.group2.sinow.presentation.allpopularcourse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemListCourseBinding
import com.group2.sinow.model.course.Course
import com.group2.sinow.utils.toCurrencyFormat

class PopularCourseAdapter(
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