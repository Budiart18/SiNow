package com.group2.sinow.presentation.course

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentCourseBinding
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.homepage.NonLoginUserDialogFragment
import com.group2.sinow.presentation.profile.ProfileViewModel
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CourseFragment : Fragment(){

    private lateinit var binding: FragmentCourseBinding

    private val courseItemAdapter: CourseItemAdapter by lazy {
        CourseItemAdapter() {
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

    private fun openNonLoginUserDialog() {
        NonLoginUserDialogFragment().show(childFragmentManager, null)
    }

    private fun navigateToDetailCourse(courseId: Int?) {
        DetailCourseActivity.startActivity(requireContext(), courseId)
    }

    private val viewModel: CourseViewModel by viewModel()

    private val profileViewModel: ProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        observeCourseList()
        setFilter()
        setupRadioButtons()
        setupSearch()
        setFilterResult()
    }

    private fun setFilterResult() {
        TODO("Not yet implemented")
    }

    private fun setupRadioButtons() {
        binding.btnAll.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery, "all")
        }

        binding.btnPremium.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery, "premium")
        }

        binding.btnFree.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery, "gratis")
        }
    }

    private fun setupSearch() {
        binding.searchBar.ivSearchButton.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.getFilterCourses(searchQuery)
        }
    }

    private fun getData() {
        viewModel.getFilterCourses()
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
                    if(it.exception is ApiException){
                        if(it.exception.httpCode == 401){
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.btnSearchCourse.isVisible = false
                        }else{
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.btnSearchCourse.isVisible = false
                        }
                    }
                }
            )
        }
    }

    private fun setFilter() {
        binding.tvFilter.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_course_to_filterDialogFragment)
        }
    }
}
