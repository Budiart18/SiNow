package com.group2.sinow.presentation.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentClassMaterialBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.detail.viewitems.DataItem
import com.group2.sinow.presentation.detail.viewitems.HeaderItem
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClassMaterialBinding.inflate(inflater, container, false)
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

    private fun observeData() {
        sharedViewModel.detailCourseData.observe(requireActivity()) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    setRv()
                    bindData(it.payload)
                },
                doOnError = { err ->
                    Toast.makeText(
                        requireContext(), err.message.orEmpty(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            val sections = item.course?.chapters?.map {
                val section = Section()
                section.setHeader(HeaderItem(it.name) { data ->
                    Toast.makeText(requireContext(), "Header Clicked : $data", Toast.LENGTH_SHORT)
                        .show()
                })
                val dataSection = it.userModules?.map { userModuleData ->
                    DataItem(userModuleData.moduleData?.name, userModuleData.status) { data ->
                        sharedViewModel.getUserModule(item.courseId, userModuleData.moduleData?.id)
                        Toast.makeText(
                            requireContext(),
                            "Item Clicked : ${userModuleData.moduleData?.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dataSection?.let { it1 -> section.addAll(it1) }
                section
            }
            sections?.let { adapter.addAll(it) }

        }
    }

}