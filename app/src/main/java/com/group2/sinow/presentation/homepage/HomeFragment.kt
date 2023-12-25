package com.group2.sinow.presentation.homepage

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.util.query
import com.faltenreich.skeletonlayout.applySkeleton
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentHomeBinding
import com.group2.sinow.presentation.account.AccountFeatureActivity
import com.group2.sinow.presentation.allpopularcourse.AllPopularCourseActivity
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.homepage.adapter.CategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.PopularCourseCategoryAdapter
import com.group2.sinow.presentation.homepage.adapter.CourseAdapter
import com.group2.sinow.presentation.notification.notificationlist.NotificationActivity
import com.group2.sinow.presentation.profile.ProfileViewModel
import com.group2.sinow.utils.SkeletonConfigWrapper
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            navigateToCourseByCategory(it.id)
        }
    }

    private val popularCourseCategoryAdapter: PopularCourseCategoryAdapter by lazy {
        PopularCourseCategoryAdapter { category ->
            homeViewModel.getCourses(category.id)
            homeViewModel.changeSelectedCategory(category)
        }
    }

    private val courseAdapter: CourseAdapter by lazy {
        CourseAdapter {
            itemCourseListener(it.id)
        }
    }

    private fun itemCourseListener(courseId: Int?) {
        profileViewModel.userData.observe(viewLifecycleOwner){resultWrapper ->
            if (resultWrapper.payload != null) {
                navigateToDetailCourse(courseId)
            } else {
                openNonLoginUserDialog()
            }
        }
    }

    private val homeViewModel: HomeViewModel by viewModel()

    private val profileViewModel: ProfileViewModel by activityViewModel()

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
        observeProfileData()
    }

    private fun observeProfileData() {
        profileViewModel.userData.observe(viewLifecycleOwner){ resultWrapper ->
            if (resultWrapper.payload != null) {
                binding.icNotification.isVisible = true
                binding.tvGreetingUser.text = getString(
                    R.string.text_hi_user,
                    resultWrapper.payload.name
                )
            } else {
                binding.icNotification.isVisible = false
                binding.tvGreetingUser.text = getString(
                    R.string.text_hi_user,
                    getString(R.string.text_sizian),
                )
            }

        }
    }

    private fun getData() {
        homeViewModel.getCategories()
        homeViewModel.getPopularCourseCategories()
        homeViewModel.getCourses()
        profileViewModel.getUserData()
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
        binding.searchBar.etSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                performSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.searchBar.ivSearchButton.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = binding.searchBar.etSearchBar.text.toString()
        navigateSearchToCourseFragment(query)
    }

    private fun navigateSearchToCourseFragment(query: String) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationCourse(query, 0)
        findNavController().navigate(action)
    }

    private fun navigateToCourseByCategory(id: Int) {
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationCourse(null, id)
        findNavController().navigate(action)
    }

    private fun navigateToDetailCourse(courseId: Int?) {
        DetailCourseActivity.startActivity(requireContext(), courseId)

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
        homeViewModel.categories.observe(viewLifecycleOwner) { resultWrapper ->
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
        homeViewModel.popularCourseCategories.observe(viewLifecycleOwner) { resultWrapper ->
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
        homeViewModel.courses.observe(viewLifecycleOwner) { resultWrapper ->
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
        homeViewModel.selectedCategory.observe(viewLifecycleOwner) {
            popularCourseCategoryAdapter.setSelectedCategory(it)
        }
    }

}