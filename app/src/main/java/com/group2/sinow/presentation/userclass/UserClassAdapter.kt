package com.group2.sinow.presentation.userclass

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.ItemListCourseProgressBinding
import com.group2.sinow.model.course.Course
import com.group2.sinow.model.userclass.UserCourseData

class UserClassAdapter(
    val itemClick: (UserCourseData) -> Unit
): RecyclerView.Adapter<UserClassViewHolder>(){

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<UserCourseData>() {
                override fun areItemsTheSame(
                    oldItem: UserCourseData,
                    newItem: UserCourseData
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: UserCourseData,
                    newItem: UserCourseData
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserClassViewHolder {
        val binding = ItemListCourseProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserClassViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: UserClassViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<UserCourseData>) {
        dataDiffer.submitList(data)
    }


}