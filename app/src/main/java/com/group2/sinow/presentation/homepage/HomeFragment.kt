package com.group2.sinow.presentation.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.group2.sinow.databinding.FragmentHomeBinding
import com.group2.sinow.presentation.homepage.adapter.CategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.CourseCategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.CourseAdapter
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter() {
            navigateToCourseByCategory()
        }
    }

    private val courseCategoryAdapter: CourseCategoryAdapter by lazy {
        CourseCategoryAdapter() {
            viewModel.getCourses(it.id)
            viewModel.changeSelectedCategory(it)
        }
    }

    private val courseAdapter: CourseAdapter by lazy {
        CourseAdapter(){
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
        observeCategoryList()
        observeCourseList()
        observePopularCourseCategoryList()
        observeSelectedCategory()
    }

    private fun getData() {
        viewModel.getCategories()
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
            navigateToAllCourse()
        }
    }

    private fun navigateToCourseByCategory() {
        TODO("Not yet implemented")
    }

    private fun navigateToDetailCourse() {
        TODO("Not yet implemented")
    }

    private fun navigateToProfile() {
        TODO("Not yet implemented")
    }

    private fun navigateToNotification() {
        TODO("Not yet implemented")
    }

    private fun navigateToAllCourse() {
        TODO("Not yet implemented")
    }

    private fun openNonLoginUserDialog() {
        NonLoginUserDialogFragment().show(childFragmentManager, null)
    }

    private fun observeCategoryList() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(
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
                    binding.layoutStateCategories.root.isVisible = true
                    binding.layoutStateCategories.loadingAnimation.isVisible = true
                    binding.layoutStateCategories.tvError.isVisible = false
                    binding.rvListCategories.isVisible = false
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

    private fun observePopularCourseCategoryList() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStatePopularCategories.root.isVisible = false
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = false
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.apply {
                        isVisible = true
                        adapter = courseCategoryAdapter
                    }
                    it.payload?.let { data -> courseCategoryAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStatePopularCategories.root.isVisible = true
                    binding.layoutStatePopularCategories.loadingAnimation.isVisible = true
                    binding.layoutStatePopularCategories.tvError.isVisible = false
                    binding.rvCategoryPopularCourse.isVisible = false
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
        viewModel.courses.observe(viewLifecycleOwner) {
            it.proceedWhen(
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
                    binding.layoutStatePopularCourse.tvError.text = "Product not found"
                    binding.rvListCourse.isVisible = false
                }
            )
        }
    }

    private fun observeSelectedCategory() {
        viewModel.selectedCategory.observe(viewLifecycleOwner) {
            courseCategoryAdapter.setSelectedCategory(it)
        }
    }

}