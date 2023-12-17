package com.group2.sinow.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.faltenreich.skeletonlayout.createSkeleton
import com.google.android.material.tabs.TabLayoutMediator
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailCourseBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.detail.player.ExoPlayerManager
import com.group2.sinow.presentation.detail.player.PlayerManager
import com.group2.sinow.utils.SkeletonConfigWrapper
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
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
        getData()
        observeData()
        setTabLayout()
        observeUserModuleData()
    }

    private fun observeData() {
        viewModel.detailCourseData.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateDetailCourse.root.isVisible = false
                    binding.layoutStateDetailCourse.loadingAnimation.isVisible = false
                    binding.layoutStateDetailCourse.tvError.isVisible = false
                    bindDetailCourse(it.payload)


                },
                doOnLoading = {
                    binding.layoutStateDetailCourse.root.isVisible = true
                    binding.layoutStateDetailCourse.loadingAnimation.isVisible = true
                    binding.layoutStateDetailCourse.tvError.isVisible = false

                },
                doOnError = {
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
            item?.course?.videoPreviewUrl?.let { playerManager.play(it) }
        }
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

    companion object {
        const val EXTRA_COURSE = "EXTRA_COURSE"

        fun startActivity(context: Context, courseId: Int?) {
            val intent = Intent(context, DetailCourseActivity::class.java)
            intent.putExtra(EXTRA_COURSE, courseId)
            context.startActivity(intent)
        }
    }
}