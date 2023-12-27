package com.group2.sinow.presentation.detail

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailCourseBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.bottom_dialog.BuyPremiumCourseDialogFragment
import com.group2.sinow.presentation.bottom_dialog.StartLearningDialogFragment
import com.group2.sinow.presentation.detail.player.ExoPlayerManager
import com.group2.sinow.presentation.detail.player.PlayerManager
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.group2.sinow.utils.toCurrencyFormat
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailCourseActivity : AppCompatActivity() {
    private val binding: ActivityDetailCourseBinding by lazy {
        ActivityDetailCourseBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailCourseViewModel by viewModel {
        parametersOf(intent.extras ?: bundleOf())
    }

    private val windowInsetsController: WindowInsetsControllerCompat by lazy {
        WindowCompat.getInsetsController(window, window.decorView)
    }

    private lateinit var playerManager: PlayerManager

    private var isFullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        observeData()
        setTabLayout()
        observeUserModuleData()
        setOnClickListener()
    }

    private fun checkFullScreen(): Boolean {
        if (checkLandscapeOrientation()) {
            changeOrientationToLandscape(false)
        } else {
            changeOrientationToLandscape(true)
        }
        return isFullScreen
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enterFullScreen()
        } else {
            exitFullScreen()
        }
    }

    private fun exitFullScreen() {
        isFullScreen = false
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        binding.llToolbar.isVisible = true
        binding.clVideoPlayerContainer.isVisible = true
        binding.videoView.isVisible = true
        binding.container.isVisible = true
        binding.layoutStateDetailCourse.root.isVisible = false
        val params =
            binding.clVideoPlayerContainer.layoutParams as ConstraintLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = 0
        params.dimensionRatio = "16:9"
        binding.clVideoPlayerContainer.layoutParams = params
    }

    private fun enterFullScreen() {
        isFullScreen = true
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        binding.llToolbar.isVisible = false
        binding.clVideoPlayerContainer.isVisible = true
        binding.videoView.isVisible = true
        binding.container.isVisible = false
        binding.layoutStateDetailCourse.root.isVisible = false
        val params =
            binding.clVideoPlayerContainer.layoutParams as ConstraintLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.clVideoPlayerContainer.layoutParams = params
    }

    fun changeOrientationToLandscape(shouldLandscape: Boolean) {
        requestedOrientation = if (shouldLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    fun checkLandscapeOrientation(): Boolean {
        val orientation = resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun setOnClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.swipeRefresh.setOnRefreshListener {
            getData()
            playerManager.release()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeData() {
        viewModel.detailCourseData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.container.isVisible = true
                    binding.btnBuyClass.isVisible = true
                    binding.layoutStateDetailCourse.root.isVisible = false
                    binding.layoutStateDetailCourse.loadingAnimation.isVisible = false
                    binding.layoutStateDetailCourse.tvError.isVisible = false
                    bindDetailCourse(it.payload)
                },
                doOnLoading = {
                    binding.layoutStateDetailCourse.root.isVisible = true
                    binding.layoutStateDetailCourse.loadingAnimation.isVisible = true
                    binding.layoutStateDetailCourse.tvError.isVisible = false
                    binding.container.isVisible = false
                    binding.btnBuyClass.isVisible = false
                },
                doOnError = {
                    binding.container.isVisible = false
                    binding.btnBuyClass.isVisible = false
                    binding.layoutStateDetailCourse.root.isVisible = true
                    binding.layoutStateDetailCourse.loadingAnimation.isVisible = false
                    binding.layoutStateDetailCourse.tvError.isVisible = true
                    if (it.exception is ApiException) {
                        Toast.makeText(
                            this,
                            it.exception.getParsedError()?.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun observeUserModuleData() {
        viewModel.userModule.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    it.payload?.videoUrl?.let { it1 ->
                        playerManager.play(it1) {
                            checkFullScreen()
                        }
                    }
                },
                doOnError = { err ->
                    if (err.exception is ApiException) {
                        Toast.makeText(
                            this,
                            err.exception.getParsedError()?.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun getData() {
        val courseId = intent.getIntExtra(EXTRA_COURSE, 0)
        viewModel.getDetailCourse(courseId)
    }

    private fun bindDetailCourse(courseData: CourseData?) {
        courseData?.let { item ->
            binding.tvTitle.text = item.course?.category?.name
            binding.tvDetailTitle.text = item.course?.name
            binding.tvBy.text = getString(
                R.string.format_course_by,
                item.course?.courseBy
            )
            binding.tvLevel.text = item.course?.level?.replaceFirstChar {
                it.uppercase()
            }
            binding.tvModul.text = getString(
                R.string.format_course_module,
                item.course?.totalModule
            )
            binding.tvTime.text = getString(
                R.string.format_course_duration,
                item.course?.totalDuration
            )
            binding.tvRating.text = item.course?.rating.toString()

            val bundle = Bundle().apply {
                putParcelable("COURSE_DATA", courseData)
            }
            val aboutClassFragment = AboutClassFragment()
            aboutClassFragment.arguments = bundle
            val materialClassFragment = ClassMaterialFragment()
            materialClassFragment.arguments = bundle

            playerManager = ExoPlayerManager(binding.videoView)
            this.lifecycle.addObserver(playerManager)
            item.course?.videoPreviewUrl?.let {
                playerManager.play(it) {
                    checkFullScreen()
                }
            }

            when (item.course?.type) {
                TYPE_GRATIS -> {
                    if (item.isFollowing == true) {
                        binding.btnBuyClass.isVisible = false
                    } else {
                        binding.btnBuyClass.isVisible = true
                        binding.btnBuyClass.text = getString(R.string.text_follow_class)
                        binding.btnBuyClass.setOnClickListener {
                            openStartLearningDialog()
                        }
                    }
                }

                TYPE_PREMIUM -> {
                    if (item.isFollowing == true && item.isAccessible == true) {
                        binding.btnBuyClass.isVisible = false
                    } else {
                        binding.btnBuyClass.isVisible = true
                        binding.btnBuyClass.text =
                            getString(
                                R.string.text_buy_class,
                                item.course.price?.toDouble()?.toCurrencyFormat()
                            )
                        binding.btnBuyClass.setOnClickListener {
                            openBuyPremiumClass()
                        }
                    }
                }
            }
        }
    }

    private fun openBuyPremiumClass() {
        BuyPremiumCourseDialogFragment().show(
            supportFragmentManager,
            "BuyPremiumCourseDialogFragment"
        )
    }

    private fun openStartLearningDialog() {
        StartLearningDialogFragment().show(supportFragmentManager, "StartLearningDialogFragment")
    }

    private fun setTabLayout() {
        val tabArray = arrayOf("Tentang", "Materi Kelas")
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = DetailViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.lifecycle.removeObserver(playerManager)
    }

    companion object {
        const val TYPE_GRATIS = "gratis"
        const val TYPE_PREMIUM = "premium"
        const val EXTRA_COURSE = "EXTRA_COURSE"

        fun startActivity(context: Context, courseId: Int?) {
            val intent = Intent(context, DetailCourseActivity::class.java)
            intent.putExtra(EXTRA_COURSE, courseId)
            context.startActivity(intent)
        }
    }
}