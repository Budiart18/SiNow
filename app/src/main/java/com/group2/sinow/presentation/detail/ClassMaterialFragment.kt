package com.group2.sinow.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentClassMaterialBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.detail.viewitems.DataItemVideoChapter
import com.group2.sinow.presentation.detail.viewitems.HeaderItemVideoChapter
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ClassMaterialFragment : Fragment() {
    private lateinit var binding: FragmentClassMaterialBinding
    private val sharedViewModel: DetailCourseViewModel by activityViewModel()

    private val adapter: GroupieAdapter by lazy {
        GroupieAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentClassMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    private fun observeData() {
        sharedViewModel.detailCourseData.observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    setRv()
                    bindData(it.payload)
                },
                doOnError = { err ->
                    if (err.exception is ApiException) {
                        if (err.exception.httpCode == 403) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.tv_toast_you_have_to_pay_first),
                                Toast.LENGTH_LONG,
                            ).show()
                        }
                        Toast.makeText(
                            requireActivity(),
                            err.exception.getParsedError()?.message.orEmpty(),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                },
            )
        }
    }

    private fun setRv() {
        binding.rvUserModule.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ClassMaterialFragment.adapter
        }
    }

    private fun bindData(item: CourseData?) {
        item?.let {
            adapter.clear()
            val sections =
                item.course?.chapters?.map {
                    val section = Section()
                    section.setHeader(
                        HeaderItemVideoChapter(it.name, it.totalDuration) { _ ->
                        },
                    )
                    val dataSection =
                        it.userModules?.map { userModuleData ->
                            DataItemVideoChapter(
                                userModuleData.moduleData?.name,
                                userModuleData.status,
                                userModuleData.moduleData?.no,
                            ) { _ ->
                                sharedViewModel.getUserModule(item.courseId, userModuleData.id)
                            }
                        }
                    dataSection?.let { it1 -> section.addAll(it1) }
                    section
                }
            sections?.let { adapter.addAll(it) }
        }
    }
}
