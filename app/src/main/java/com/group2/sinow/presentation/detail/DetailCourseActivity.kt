package com.group2.sinow.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.android.material.tabs.TabLayoutMediator
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailCourseBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.bottom_dialog.BuyPremiumCourseDialogFragment
import com.group2.sinow.presentation.bottom_dialog.StartLearningDialogFragment
import com.group2.sinow.presentation.detail.player.ExoPlayerManager
import com.group2.sinow.presentation.detail.player.PlayerManager
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.utils.SkeletonConfigWrapper
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

    private lateinit var playerManager: PlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeData()
        setTabLayout()
        observeUserModuleData()
        setOnClickListener()
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
                    it.payload?.videoUrl?.let { it1 -> playerManager.play(it1) }
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
            item.course?.videoPreviewUrl?.let { playerManager.play(it) }

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
        BuyPremiumCourseDialogFragment().show(supportFragmentManager, "BuyPremiumCourseDialogFragment")
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