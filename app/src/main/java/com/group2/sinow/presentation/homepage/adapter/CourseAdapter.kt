package com.group2.sinow.presentation.homepage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.ItemGridCourseBinding
import com.group2.sinow.model.course.Course
import com.group2.sinow.presentation.homepage.viewholder.ItemGridCourseViewHolder

class CourseAdapter(
    private val itemClick: (Course) -> Unit
) : RecyclerView.Adapter<ItemGridCourseViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGridCourseViewHolder {
        val binding = ItemGridCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemGridCourseViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemGridCourseViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<Course>) {
        dataDiffer.submitList(data)
    }

}