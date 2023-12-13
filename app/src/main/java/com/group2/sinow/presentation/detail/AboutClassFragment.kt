package com.group2.sinow.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailCourseBinding
import com.group2.sinow.databinding.FragmentAboutClassBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AboutClassFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        observeData()
    }

    private fun getData() {
        sharedViewModel.getDetailCourse(sharedViewModel.detail ?: 0)
    }

    private fun bindData(courseData: CourseData?) {
        courseData.let { item ->
            binding.tvContentAboutClass.text = item?.course?.description

        }
    }

    private fun observeData() {
        sharedViewModel.detailCourseData.observe(requireActivity()) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    bindData(it.payload)
                },
                doOnError = { error ->
                    binding.tvContentAboutClass.text = error.exception?.message.orEmpty()
                }
            )

        }
    }

    private lateinit var binding: FragmentAboutClassBinding
    private val sharedViewModel: DetailCourseViewModel by activityViewModel()
}