package com.group2.sinow.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.group2.sinow.R
import com.group2.sinow.databinding.ActivityDetailCourseBinding
import com.group2.sinow.databinding.FragmentAboutClassBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.detail.adapter.BenefitListAdapter
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AboutClassFragment : Fragment() {

    private lateinit var binding: FragmentAboutClassBinding
    private val sharedViewModel: DetailCourseViewModel by activityViewModel()
    private val benefitAdapter: BenefitListAdapter by lazy {
        BenefitListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setUpRv()
    }

    private fun setUpRv() {
        binding.rvBenefitList.apply {
            adapter = benefitAdapter
        }
    }

    private fun bindData(courseData: CourseData?) {
        courseData.let { item ->
            binding.tvContentAboutClass.text = item?.course?.description
            binding.rvBenefitList.apply {
                adapter = benefitAdapter

            }
            item?.course?.benefits?.let {
                benefitAdapter.submitData(it)
            }
        }
    }

    private fun observeData() {
        sharedViewModel.detailCourseData.observe(requireActivity()) { result ->
            result.proceedWhen(
                doOnSuccess = { resultWrapper ->
                    bindData(resultWrapper.payload)
                },
                doOnError = {
                    if (it.exception is ApiException) {
                        Toast.makeText(
                            requireContext(),
                            it.exception.getParsedError()?.message.orEmpty(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

        }
    }

}