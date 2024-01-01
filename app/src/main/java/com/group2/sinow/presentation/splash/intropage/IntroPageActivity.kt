package com.group2.sinow.presentation.splash.intropage

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityIntroSliderBinding
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.main.MainActivity
import com.group2.sinow.presentation.splash.SplashViewModel
import com.group2.sinow.utils.highLightWord
import org.koin.androidx.viewmodel.ext.android.viewModel

class IntroPageActivity : AppCompatActivity() {

    private val binding: ActivityIntroSliderBinding by lazy {
        ActivityIntroSliderBinding.inflate(layoutInflater)
    }

    private val viewModel: SplashViewModel by viewModel()

    private val introPageAdapter: IntroPageAdapter by lazy {
        IntroPageAdapter(viewModel.getIntroPageData())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewpagerIntroSlider.adapter = introPageAdapter
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
            viewModel.setShouldShowIntroPage(false)
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun handleNextButtonClick() {
        if (binding.viewpagerIntroSlider.currentItem + 1 < introPageAdapter.itemCount) {
            binding.viewpagerIntroSlider.currentItem += 1
        } else {
            viewModel.setShouldShowIntroPage(false)
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(introPageAdapter.itemCount)
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
