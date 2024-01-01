package com.group2.sinow.presentation.course

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentCourseBinding
import com.group2.sinow.model.category.Category
import com.group2.sinow.presentation.course.filtercourse.FilterDialogFragment
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.presentation.main.MainViewModel
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseFragment : Fragment(), FilterDialogFragment.OnFilterListener {

    private lateinit var binding: FragmentCourseBinding

    private val filterDialogFragment: FilterDialogFragment by lazy {
        FilterDialogFragment()
    }

    private val viewModel: CourseViewModel by viewModel()

    private val mainViewModel: MainViewModel by activityViewModel()

    private var searchQuery: String? = null
    private var selectedType: String? = null
    private var selectedCategories: List<Category>? = null
    private var selectedLevel: List<String>? = null
    private var selectedSortBy: String? = null

    private val courseItemAdapter: CourseItemAdapter by lazy {
        CourseItemAdapter() {
            itemCourseListener(it.id)
        }
    }

    private fun itemCourseListener(courseId: Int?) {
        mainViewModel.userData.observe(viewLifecycleOwner) { resultWrapper ->
            if (resultWrapper.payload != null) {
                navigateToDetailCourse(courseId)
            } else {
                openNonLoginUserDialog()
            }
        }
    }

    private fun openNonLoginUserDialog() {
        NonLoginUserDialogFragment().show(childFragmentManager, null)
    }

    private fun navigateToDetailCourse(courseId: Int?) {
        DetailCourseActivity.startActivity(requireContext(), courseId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCourseList()
        openFilterDialog()
        setType()
        setupSearch()
        observeFilterData()
        receivedArguments()
        refreshData()
        buildChipItem()
        viewModel.resetFilter()
    }
    private fun buildChipItem() {
        if (selectedCategories != null) {
            selectedCategories?.map {
                addChipToGroup(it.categoryName)
            }
        }
        if (selectedLevel != null) {
            selectedLevel?.map {
                addChipToGroup(it)
            }
        }
        if (selectedSortBy != null) addChipToGroup(selectedSortBy)
    }

    private fun addChipToGroup(chipItem: String?) {
        if (!chipItem.isNullOrBlank()) {
            val chip = Chip(context, null, R.attr.CustomChipChoiceStyle)
            chip.text = chipItem
            chip.isChipIconVisible = false
            chip.isCheckable = false
            binding.chipGroup.addView(chip as View)
        }
    }

    private fun refreshData() {
        binding.swipeRefresh.setOnRefreshListener {
            getData(searchQuery, selectedType, selectedCategories, selectedLevel, selectedSortBy)
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun receivedArguments() {
        val category = arguments?.getParcelable<Category>(KEY_CATEGORY)
        if (category == null) {
            selectedCategories = null
        } else {
            selectedCategories = listOf(category)
            viewModel.addSelectedCategory(category)
        }
        val query = arguments?.getString(KEY_QUERY)
        if (!query.isNullOrEmpty()) {
            searchQuery = query
            viewModel.setSearchQuery(query)
            binding.searchBar.etSearchBar.setText(query)
        } else {
            searchQuery = null
        }
        getData(searchQuery, selectedType, selectedCategories, selectedLevel, selectedSortBy)
        arguments = null
    }

    private fun observeFilterData() {
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            searchQuery = query
            getData(searchQuery, selectedType, selectedCategories, selectedLevel, selectedSortBy)
        }
        viewModel.selectedType.observe(viewLifecycleOwner) { type ->
            selectedType = type
            getData(searchQuery, selectedType, selectedCategories, selectedLevel, selectedSortBy)
        }
    }

    private fun setType() {
        binding.btnAll.setOnClickListener {
            viewModel.setSelectedType(TYPE_ALL)
        }
        binding.btnPremium.setOnClickListener {
            viewModel.setSelectedType(TYPE_PREMIUM)
        }
        binding.btnFree.setOnClickListener {
            viewModel.setSelectedType(TYPE_FREE)
        }
    }

    private fun getData(
        search: String? = null,
        type: String? = null,
        category: List<Category>? = null,
        level: List<String>? = null,
        sortBy: String? = null
    ) {
        val categoryIdList = category?.map {
            it.id
        }
        viewModel.getCourses(search, type, categoryIdList, level, sortBy)
    }

    private fun setupSearch() {
        binding.searchBar.etSearchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val searchQuery = binding.searchBar.etSearchBar.text.toString()
                viewModel.setSearchQuery(searchQuery)
                this.searchQuery = searchQuery
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.searchBar.ivSearchButton.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.setSearchQuery(searchQuery)
            this.searchQuery = searchQuery
        }
    }

    private fun observeCourseList() {
        viewModel.courses.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateClassTopic.root.isVisible = false
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = false
                    binding.layoutStateClassTopic.tvError.isVisible = false
                    binding.layoutCourseEmpty.root.isVisible = false
                    binding.rvListCourse.apply {
                        isVisible = true
                        adapter = courseItemAdapter
                    }
                    it.payload?.let { data -> courseItemAdapter.submitData(data) }
                },
                doOnLoading = {
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = true
                    binding.layoutStateClassTopic.tvError.isVisible = false
                    binding.rvListCourse.isVisible = false
                    binding.layoutCourseEmpty.root.isVisible = false
                },
                doOnError = {
                    binding.layoutStateClassTopic.root.isVisible = true
                    binding.layoutStateClassTopic.loadingAnimation.isVisible = false
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

    private fun openFilterDialog() {
        binding.tvFilter.setOnClickListener {
            filterDialogFragment.setFilterListener(this)
            filterDialogFragment.show(childFragmentManager, TAG_DIALOG)
        }
    }

    override fun onFilterApplied(
        search: String?,
        type: String?,
        category: List<Category>?,
        level: List<String>?,
        sortBy: String?
    ) {
        searchQuery = search
        selectedCategories = category
        selectedLevel = level
        selectedSortBy = sortBy
        val categoryIdList = category?.map {
            it.id
        }
        viewModel.getCourses(searchQuery, selectedType, categoryIdList, level, sortBy)
        binding.chipGroup.removeAllViews()
        buildChipItem()
    }

    companion object {
        const val TYPE_ALL = "all"
        const val TYPE_PREMIUM = "premium"
        const val TYPE_FREE = "gratis"
        const val KEY_QUERY = "searchQuery"
        const val KEY_CATEGORY = "selectedCategory"
        const val TAG_DIALOG = "FilterDialog"
    }
}
