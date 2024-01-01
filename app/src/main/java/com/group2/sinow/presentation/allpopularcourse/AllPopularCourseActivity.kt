package com.group2.sinow.presentation.allpopularcourse

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.faltenreich.skeletonlayout.applySkeleton
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityAllPopularCourseBinding
import com.group2.sinow.presentation.allpopularcourse.adapter.PopularCourseAdapter
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.presentation.homepage.adapter.PopularCourseCategoryAdapter
import com.group2.sinow.utils.SkeletonConfigWrapper
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllPopularCourseActivity : AppCompatActivity() {

    private val binding: ActivityAllPopularCourseBinding by lazy {
        ActivityAllPopularCourseBinding.inflate(layoutInflater)
    }

    private val viewModel: AllPopularCourseViewModel by viewModel()

    private val categoryAdapter: PopularCourseCategoryAdapter by lazy {
        PopularCourseCategoryAdapter {
            viewModel.changeSelectedCategory(it)
        }
    }

    private val courseAdapter: PopularCourseAdapter by lazy {
        PopularCourseAdapter {
            itemCourseListener(it.id)
        }
    }

    private fun itemCourseListener(courseId: Int?) {
        viewModel.userData.observe(this) { resultWrapper ->
            if (resultWrapper.payload != null) {
                navigateToDetailCourse(courseId)
            } else {
                openNonLoginUserDialog()
            }
        }
    }


    private fun navigateToDetailCourse(courseId: Int?) {
        DetailCourseActivity.startActivity(this, courseId)
    }

    private var searchQuery: String? = null
    private var selectedCategory: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
        observePopularCourseCategoryList()
        observeSelectedCategory()
        observeCourseList()
        observeFilterData()
        setupSearch()
        setClickListener()
    }

    private fun observeFilterData() {
        viewModel.searchQuery.observe(this) { query ->
            searchQuery = query
            getCourseData(searchQuery, selectedCategory)
        }
        viewModel.selectedCategory.observe(this) { category ->
            selectedCategory = category.id
            getCourseData(searchQuery, selectedCategory)
        }
    }

    private fun setClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.swipeRefresh.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        getCourseData(searchQuery, selectedCategory)
        binding.swipeRefresh.isRefreshing = false
    }

    private fun setupSearch() {
        binding.searchBar.etSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val searchQuery = binding.searchBar.etSearchBar.text.toString()
                viewModel.setSearchQuery(searchQuery)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.searchBar.ivSearchButton.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.setSearchQuery(searchQuery)
        }
    }

    private fun getData() {
        viewModel.getUserData()
        viewModel.getCategories()
        getCourseData()
    }

    private fun getCourseData(search: String? = null, category: Int? = null) {
        viewModel.getCourses(search, category)
    }

    private fun observePopularCourseCategoryList() {
        viewModel.categories.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.rvCategoryPopularCourse.apply {
                        isVisible = true
                        adapter = categoryAdapter
                    }
                    it.payload?.let { data -> categoryAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.rvCategoryPopularCourse.isVisible = true
                    binding.rvCategoryPopularCourse.applySkeleton(
                        R.layout.item_list_category,
                        itemCount = 8,
                        SkeletonConfigWrapper(this).customSkeletonConfig()
                    ).showSkeleton()
                },
                doOnError = {
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
                    binding.layoutCourseEmpty.root.isVisible = false
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
                    binding.layoutCourseEmpty.root.isVisible = false
                    binding.rvListCourse.isVisible = false
                },
                doOnError = {
                    binding.layoutStatePopularCourse.root.isVisible = true
                    binding.layoutStatePopularCourse.loadingAnimation.isVisible = false
                    binding.rvListCourse.isVisible = false
                    if (it.exception is ApiException) {
                        if (it.exception.httpCode == 401) {
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.btnSearchCourse.isVisible = false
                        } else {
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.btnSearchCourse.isVisible = false
                        }
                    }
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
