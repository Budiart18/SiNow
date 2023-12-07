package com.group2.sinow.presentation.introslider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.group2.sinow.presentation.main.MainActivity
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityIntroSliderBinding
import com.group2.sinow.model.introslider.IntroSliderItem
import com.group2.sinow.utils.highLightWord

class IntroSliderActivity : AppCompatActivity() {

    private val binding: ActivityIntroSliderBinding by lazy {
        ActivityIntroSliderBinding.inflate(layoutInflater)
    }

    private val introSliderItems: List<IntroSliderItem> = listOf(
        IntroSliderItem("SINOW", "Sinau dari pengalaman", R.drawable.introslider_icon1),
        IntroSliderItem("SINOW", "Sinau dari mentor", R.drawable.introslider_icon2),
        IntroSliderItem("SINOW", "Sinau darimana saja", R.drawable.introslider_icon3),
    )

    private val introSliderAdapter :IntroSliderAdapter by lazy {
        IntroSliderAdapter(introSliderItems)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewpagerIntroSlider.adapter = introSliderAdapter
        setupIndicators()
        setCurrentIndicator(0)
        changePageViewPager()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnNext.setOnClickListener {
            handleNextButtonClick()
        }
        binding.tvGoToLogin.highLightWord(getString(R.string.text_highlight_login_here)) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun handleNextButtonClick() {
        if (binding.viewpagerIntroSlider.currentItem + 1 < introSliderAdapter.itemCount) {
            binding.viewpagerIntroSlider.currentItem += 1
        } else {
            navigateToMain()
        }
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introSliderAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.slider_indicator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.indicatorSlider.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.indicatorSlider.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorSlider[i] as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.slider_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.slider_indicator_inactive
                    )
                )
            }
        }
    }

    private fun changePageViewPager() {
        binding.viewpagerIntroSlider.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )
    }
}