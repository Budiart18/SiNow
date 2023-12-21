package com.group2.sinow.presentation.course.filtercourse

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.databinding.FragmentFilterDialogBinding
import com.group2.sinow.databinding.FragmentFilterDialogBinding.inflate
import com.group2.sinow.model.category.Category
import com.group2.sinow.presentation.course.CourseFragment
import com.group2.sinow.presentation.course.CourseViewModel
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FilterDialogFragment : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentFilterDialogBinding

    private val viewModel: CourseViewModel by activityViewModel()

    private val categoryAdapter: CategoryFilterAdapter by lazy {
        CategoryFilterAdapter(object : CategoryItemListener {
            override fun onCategoryChecked(category: Category) {
                viewModel.onCategoryChecked(category)
                Log.d("ADAPTER", "onCategoryChecked: ${viewModel.getSelectedCategory().toString()} ")
            }
            override fun onCategoryUnchecked(category: Category) {
                viewModel.onCategoryUncheck(category)
            }
        }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun getExpandedHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        println(screenHeight)
        return (screenHeight * 0.9).toInt()
    }

    override fun isSheetAlwaysExpanded(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        observeCategoryList()
        setClickListener()
    }


    private fun setClickListener() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.btnFilter.setOnClickListener {
            applyFilter()
            dismiss()
        }
    }

    private fun applyFilter() {
    }


    private fun getData() {
        viewModel.getCategories()
    }

    private fun observeCategoryList() {
        viewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.rvFilterCategory.apply {
                        adapter = categoryAdapter
                    }
                    it.payload?.let { data -> categoryAdapter.submitData(data) }
                }
            )
        }
    }

}