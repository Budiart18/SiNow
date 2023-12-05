package com.group2.sinow.presentation.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemListNotificationBinding
import com.group2.sinow.model.notification.Notification

class NotificationAdapter(private val itemClick: (Notification) -> Unit) :
    RecyclerView.Adapter<ItemNotificationViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object  : DiffUtil.ItemCallback<Notification>() {
                override fun areItemsTheSame(
                    oldItem: Notification,
                    newItem: Notification
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Notification,
                    newItem: Notification
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }

            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemNotificationViewHolder {
        val binding = ItemListNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return ItemNotificationViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ItemNotificationViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<Notification>) {
        dataDiffer.submitList(data)
    }

}

class ItemNotificationViewHolder(
    private val binding: ItemListNotificationBinding,
    val itemClick: (Notification) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Notification){
        binding.tvNotificationTitle.text = item.title
        binding.tvNotificationCategory.text = item.type
        binding.tvNotificationDesc.text = item.content
        binding.icNotification.setImageResource(
            if (item.isRead == true) R.drawable.ic_notification_read else R.drawable.ic_notification_unread
        )
        binding.tvNotificationTime.text = item.createdAt
        binding.root.setBackgroundResource(
            if (item.isRead == true) android.R.color.transparent else R.color.app_color_primary_container
        )
        binding.root.setOnClickListener {
            itemClick.invoke(item)
        }
    }

}