package com.group2.sinow.presentation.userclass

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentUserClassBinding
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserClassFragment : Fragment() {

    private lateinit var binding: FragmentUserClassBinding

    private val userClassAdapter: UserClassAdapter by lazy {
        UserClassAdapter {
            itemCourseListener(it.courseId)
        }
    }

    private fun itemCourseListener(courseId: Int?) {
        DetailCourseActivity.startActivity(requireContext(), courseId)
    }

    private val viewModel: UserClassViewModel by viewModel()

    private var searchQuery: String? = null
    private var selectedProgress: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        observeCourseList()
        setProgress()
        setupSearch()
        observeFilterData()
        refreshData()
    }

    private fun fetchData() {
        getData(searchQuery, selectedProgress)
    }

    private fun refreshData() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeFilterData() {
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            searchQuery = query
            fetchData()
        }
        viewModel.selectedProgress.observe(viewLifecycleOwner) { progress ->
            selectedProgress = progress
            fetchData()
        }
    }

    private fun getData(search: String? = null, progress: String? = null) {
        viewModel.getUserCourses(search, progress)
    }

    private fun setProgress() {
        binding.btnAll.setOnClickListener {
            viewModel.setSelectedProgress(PROGRESS_ALL)
        }

        binding.btnInProgress.setOnClickListener {
            viewModel.setSelectedProgress(ON_PROGRESS)
        }

        binding.btnFinish.setOnClickListener {
            viewModel.setSelectedProgress(PROGRESS_FINISH)
        }
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

    private fun observeCourseList() {
        viewModel.courses.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.layoutStateClassUser.root.isVisible = false
                    binding.layoutStateClassUser.loadingAnimation.isVisible = false
                    binding.layoutStateClassUser.tvError.isVisible = false
                    binding.layoutCourseEmpty.root.isVisible = false
                    binding.rvListUserCourse.apply {
                        isVisible = true
                        adapter = userClassAdapter
                    }
                    it.payload?.let { data ->
                        userClassAdapter.submitData(data)
                    }
                },
                doOnLoading = {
                    binding.layoutStateClassUser.root.isVisible = true
                    binding.layoutStateClassUser.loadingAnimation.isVisible = true
                    binding.layoutStateClassUser.tvError.isVisible = false
                    binding.rvListUserCourse.isVisible = false
                    binding.layoutCourseEmpty.root.isVisible = false
                },
                doOnError = {
                    binding.layoutStateClassUser.root.isVisible = true
                    binding.layoutStateClassUser.loadingAnimation.isVisible = false
                    binding.rvListUserCourse.isVisible = false
                    if (it.exception is ApiException) {
                        if (it.exception.httpCode == 401) {
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.tvNotificationEmpty.text = getString(R.string.tv_user_must_login_first)
                        } else {
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.tvNotificationEmpty.text = getString(R.string.tv_you_havent_taken_course_yet)
                        }
                    }
                }
            )
        }
    }

    companion object {
        const val PROGRESS_ALL = "all"
        const val ON_PROGRESS = "inProgress"
        const val PROGRESS_FINISH = "completed"
    }
}
