package com.group2.sinow.presentation.allpopularcourse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.faltenreich.skeletonlayout.applySkeleton
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityAllPopularCourseBinding
import com.group2.sinow.presentation.allpopularcourse.adapter.PopularCourseAdapter
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.presentation.homepage.adapter.PopularCourseCategoryAdapter
import com.group2.sinow.utils.SkeletonConfigWrapper
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllPopularCourseActivity : AppCompatActivity() {

    private val binding : ActivityAllPopularCourseBinding by lazy {
        ActivityAllPopularCourseBinding.inflate(layoutInflater)
    }

    private val viewModel : AllPopularCourseViewModel by viewModel()

    private val categoryAdapter: PopularCourseCategoryAdapter by lazy {
        PopularCourseCategoryAdapter {
            viewModel.getCourses(category = it.id)
            viewModel.changeSelectedCategory(it)
        }
    }

    private val courseAdapter: PopularCourseAdapter by lazy {
        PopularCourseAdapter {
            openNonLoginUserDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
        observePopularCourseCategoryList()
        observeSelectedCategory()
        observeCourseList()
        setClickListener()
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getCourses()
    }

    private fun observePopularCourseCategoryList() {
        viewModel.categories.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStatePopularCategories.root.isVisible = false
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.apply {
                        isVisible = true
                        adapter = categoryAdapter
                    }
                    it.payload?.let { data -> categoryAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStatePopularCategories.root.isVisible = false
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.isVisible = true
                    binding.rvCategoryPopularCourse.applySkeleton(
                        R.layout.item_list_category,
                        itemCount = 8,
                        SkeletonConfigWrapper(this).customSkeletonConfig()
                    ).showSkeleton()
                },
                doOnError = {
                    binding.layoutStatePopularCategories.root.isVisible = true
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = true
                    binding.layoutStatePopularCategories.tvError.text = it.exception?.message.orEmpty()
                    binding.rvCategoryPopularCourse.isVisible = false
                }
            )
        }
    }

    private fun observeCourseList() {
        viewModel.courses.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStatePopularCourse.root.isVisible = false
                    binding.layoutStatePopularCourse.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCourse.tvError.isVisible = false
                    binding.rvListCourse.apply {
                        isVisible = true
                        adapter = courseAdapter
                    }
                    it.payload?.let { data -> courseAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStatePopularCourse.root.isVisible = true
                    binding.layoutStatePopularCourse.loadingAnimation.isVisible = true
                    binding.layoutStatePopularCourse.tvError.isVisible = false
                    binding.rvListCourse.isVisible = false
                },
                doOnError = {
                    binding.layoutStatePopularCourse.root.isVisible = true
                    binding.layoutStatePopularCourse.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCourse.tvError.isVisible = true
                    binding.layoutStatePopularCourse.tvError.text = it.exception?.message.orEmpty()
                    binding.rvListCourse.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutStatePopularCourse.root.isVisible = true
                    binding.layoutStatePopularCourse.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCourse.tvError.isVisible = true
                    binding.layoutStatePopularCourse.tvError.text = getString(R.string.text_course_not_found)
                    binding.rvListCourse.isVisible = false
                }
            )
        }
    }

    private fun observeSelectedCategory() {
        viewModel.selectedCategory.observe(this) {
            categoryAdapter.setSelectedCategory(it)
        }
    }

    private fun openNonLoginUserDialog() {
        NonLoginUserDialogFragment().show(supportFragmentManager, null)
    }

}