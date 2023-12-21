package com.group2.sinow.presentation.userclass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentUserClassBinding
import com.group2.sinow.presentation.course.CourseViewModel
import com.group2.sinow.presentation.detail.DetailCourseActivity
import com.group2.sinow.presentation.notification.notificationdetail.NotificationDetailActivity
import com.group2.sinow.presentation.notification.notificationlist.NotificationActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserClassFragment : Fragment() {

    private lateinit var binding: FragmentUserClassBinding

    private val userClassAdapter: UserClassAdapter by lazy {
        UserClassAdapter{
            itemCourseListener(it.courseId)
        }
    }

    private fun itemCourseListener(courseId: Int?) {
        DetailCourseActivity.startActivity(requireContext(), courseId)
    }


    private val viewModel: UserClassViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCourseList()
        getData()
        setupRadioButtons()
        setupSearch()
    }

    private fun getData() {
        viewModel.getUserCourses()
    }

    private fun setupRadioButtons() {
        binding.btnAll.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery, "all")
        }

        binding.btnInProgress.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery,"inProgress")
        }

        binding.btnFinish.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.onTabClicked(searchQuery,"completed")
        }
    }

    private fun setupSearch() {
        binding.searchBar.ivSearchButton.setOnClickListener {
            val searchQuery = binding.searchBar.etSearchBar.text.toString()
            viewModel.getUserCourses(searchQuery)
        }
    }

    private fun observeCourseList() {
        viewModel.courses.observe(viewLifecycleOwner){
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
                    if(it.exception is ApiException){
                        if(it.exception.httpCode == 401){
                            binding.layoutCourseEmpty.root.isVisible = true
                            binding.layoutCourseEmpty.tvNotificationEmpty.text = getString(R.string.tv_user_must_login_first)
                        }else{
                            binding.layoutCourseEmpty.root.isVisible = true
                        }
                    }
                }
            )
        }
    }

}