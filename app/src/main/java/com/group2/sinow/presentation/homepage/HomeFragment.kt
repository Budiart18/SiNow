package com.group2.sinow.presentation.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.faltenreich.skeletonlayout.applySkeleton
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentHomeBinding
import com.group2.sinow.presentation.account.AccountFeatureActivity
import com.group2.sinow.presentation.allpopularcourse.AllPopularCourseActivity
import com.group2.sinow.presentation.homepage.adapter.CategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.PopularCourseCategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.CourseAdapter
import com.group2.sinow.presentation.notification.notificationlist.NotificationActivity
import com.group2.sinow.presentation.profile.ProfileActivity
import com.group2.sinow.utils.SkeletonConfigWrapper
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            navigateToCourseByCategory()
        }
    }

    private val popularCourseCategoryAdapter: PopularCourseCategoryAdapter by lazy {
        PopularCourseCategoryAdapter { category ->
            viewModel.getCourses(category.id)
            viewModel.changeSelectedCategory(category)
        }
    }

    private val courseAdapter: CourseAdapter by lazy {
        CourseAdapter {
            //navigateToDetailCourse()
            openNonLoginUserDialog()
        }
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setClickListener()
        observeCategoryData()
        observeCourseData()
        observePopularCourseCategoryData()
        observeSelectedCategory()
    }

    private fun getData() {
        viewModel.getCategories()
        viewModel.getPopularCourseCategories()
        viewModel.getCourses()
    }

    private fun setClickListener() {
        binding.icProfile.setOnClickListener {
            navigateToProfile()
        }
        binding.icNotification.setOnClickListener {
            navigateToNotification()
        }
        binding.tvSeeAllCourse.setOnClickListener {
            navigateToAllPopularCourse()
        }
    }

    private fun navigateToCourseByCategory() {
        TODO("Not yet implemented")
    }

    private fun navigateToDetailCourse() {
        TODO("Not yet implemented")
    }

    private fun navigateToProfile() {
        val intent = Intent(requireContext(), AccountFeatureActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNotification() {
        val intent = Intent(requireContext(), NotificationActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAllPopularCourse() {
        val intent = Intent(requireContext(), AllPopularCourseActivity::class.java)
        startActivity(intent)
    }

    private fun openNonLoginUserDialog() {
        NonLoginUserDialogFragment().show(childFragmentManager, null)
    }

    private fun observeCategoryData() {
        viewModel.categories.observe(viewLifecycleOwner) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateCategories.root.isVisible = false
                    binding.layoutStateCategories.loadingAnimation.isVisible = false
                    binding.layoutStateCategories.tvError.isVisible = false
                    binding.rvListCategories.apply {
                        isVisible = true
                        adapter = categoryAdapter
                    }
                    it.payload?.let { data -> categoryAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStateCategories.root.isVisible = false
                    binding.layoutStateCategories.loadingAnimation.isVisible = false
                    binding.layoutStateCategories.tvError.isVisible = false
                    binding.rvListCategories.isVisible = true
                    binding.rvListCategories.applySkeleton(
                        R.layout.item_grid_categories,
                        itemCount = 8,
                        SkeletonConfigWrapper(requireContext()).customSkeletonConfig()
                    ).showSkeleton()
                },
                doOnError = {
                    binding.layoutStateCategories.root.isVisible = true
                    binding.layoutStateCategories.loadingAnimation.isVisible = false
                    binding.layoutStateCategories.tvError.isVisible = true
                    binding.layoutStateCategories.tvError.text = it.exception?.message.orEmpty()
                    binding.rvListCategories.isVisible = false
                }
            )
        }
    }

    private fun observePopularCourseCategoryData() {
        viewModel.popularCourseCategories.observe(viewLifecycleOwner) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutStatePopularCategories.root.isVisible = false
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.apply {
                        isVisible = true
                        adapter = popularCourseCategoryAdapter
                    }
                    it.payload?.let { data -> popularCourseCategoryAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStatePopularCategories.root.isVisible = false
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.isVisible = true
                    binding.rvCategoryPopularCourse.applySkeleton(
                        R.layout.item_list_category,
                        itemCount = 8,
                        SkeletonConfigWrapper(requireContext()).customSkeletonConfig()
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

    private fun observeCourseData() {
        viewModel.courses.observe(viewLifecycleOwner) { resultWrapper ->
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
                    binding.layoutStatePopularCourse.tvError.text =
                        getString(R.string.text_course_not_found)
                    binding.rvListCourse.isVisible = false
                }
            )
        }
    }

    private fun observeSelectedCategory() {
        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            popularCourseCategoryAdapter.setSelectedCategory(it)
        }
    }

}