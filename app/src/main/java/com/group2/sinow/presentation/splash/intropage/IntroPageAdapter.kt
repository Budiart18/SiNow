package com.group2.sinow.presentation.splash.intropage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.group2.sinow.databinding.IntroSliderItemBinding
import com.group2.sinow.model.introslider.IntroSliderItem

class IntroPageAdapter(
    private val introSlide: List<IntroSliderItem>
) : RecyclerView.Adapter<IntroPageAdapter.IntroSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        val binding = IntroSliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IntroSliderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return introSlide.size
    }

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(introSlide[position])
    }

    inner class IntroSliderViewHolder(
        private val binding: IntroSliderItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(introSliderItem: IntroSliderItem) {
            binding.tvFirstText.text = introSliderItem.title
            binding.tvSecondText.text = introSliderItem.desc
            binding.ivSlideIcon.setImageResource(introSliderItem.icon)
        }
    }
}
