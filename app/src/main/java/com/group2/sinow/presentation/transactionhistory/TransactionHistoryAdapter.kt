package com.group2.sinow.presentation.transactionhistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.group2.sinow.R
import com.group2.sinow.databinding.ItemListCourseBinding
import com.group2.sinow.model.paymenthistory.TransactionUser

class PaymentHistoryAdapter(
    val itemClick: (TransactionUser) -> Unit) :
    RecyclerView.Adapter<PaymentHistoryViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<TransactionUser>() {
                override fun areItemsTheSame(
                    oldItem: TransactionUser,
                    newItem: TransactionUser
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: TransactionUser,
                    newItem: TransactionUser
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        val binding = ItemListCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentHistoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        holder.bind(dataDiffer.currentList[position])
    }

    fun submitData(data: List<TransactionUser>) {
        dataDiffer.submitList(data)
    }
}

class PaymentHistoryViewHolder(
    val binding: ItemListCourseBinding,
    val itemClick: (TransactionUser) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val STATUS_SUCCESS = "SUDAH_BAYAR"
        private const val STATUS_EXPIRED = "KADALUARSA"
        private const val STATUS_WAITING = "BELUM_BAYAR"


    }

    fun bind(item: TransactionUser) {
        with(item) {
            binding.ivCourseImage.load(item.courseUser?.imageUrl) {
                crossfade(true)
            }

            binding.tvCourseCategory.text = item.courseUser?.category?.name
            binding.tvCourseRate.text = item.courseUser?.rating.toString()
            binding.tvCourseName.text = item.courseUser?.name
            binding.tvCourseAuthor.text = itemView.rootView.context.getString(
                R.string.format_course_by,
                item.courseUser?.courseBy
            )
            binding.tvCourseLevel.text = item.courseUser?.level?.replaceFirstChar {
                it.uppercase()
            }
            binding.tvCourseDuration.text = itemView.rootView.context.getString(
                R.string.format_course_duration,
                item.courseUser?.totalDuration
            )

            binding.tvCourseModules.text = itemView.rootView.context.getString(
                R.string.format_course_module,
                item.courseUser?.totalModule
            )

            binding.tvBtnBuy.text = item.courseUser?.type?.replaceFirstChar {
                it.uppercase()
            }
            binding.ivPremium.visibility = View.VISIBLE

            when (item.status) {
                STATUS_SUCCESS -> {
                    binding.tvBtnBuy.text = itemView.rootView.context.getString(R.string.status_success)
                    binding.btnBuy.run { setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_success)) }
                }
                STATUS_WAITING -> {
                    binding.tvBtnBuy.text = itemView.rootView.context.getString(R.string.status_waiting_payment)
                    binding.btnBuy.run { setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_waiting)) }
                }
                else -> {
                    binding.tvBtnBuy.text = itemView.rootView.context.getString(R.string.status_failed)
                    binding.btnBuy.run { setCardBackgroundColor(ContextCompat.getColor(context, R.color.color_warning)) }
                }
            }


            itemView.setOnClickListener {
                itemClick(item)
            }

        }

    }
}
