package com.group2.sinow.presentation.course.filtercourse

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentFilterDialogBinding
import com.group2.sinow.model.category.Category
import com.group2.sinow.presentation.course.CourseViewModel
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.getViewModel

class FilterDialogFragment : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentFilterDialogBinding

    private val viewModel: CourseViewModel by lazy { requireParentFragment().getViewModel() }

    private val categoryAdapter: CategoryFilterAdapter by lazy {
        CategoryFilterAdapter(object : CategoryItemListener {
            override fun onCategoryChecked(category: Category) {
                viewModel.addSelectedCategory(category)
            }
            override fun onCategoryUnchecked(category: Category) {
                viewModel.removeSelectedCategory(category)
            }
            override fun getSelectedCategories(): List<Category>? {
                return viewModel.selectedCategories.value
            }
        })
    }

    private var isBeginnerChecked: Boolean = false
    private var isIntermediateChecked: Boolean = false
    private var isAdvancedChecked: Boolean = false

    private var filterListener: OnFilterListener? = null

    interface OnFilterListener {
        fun onFilterApplied(
            search: String?,
            type: String?,
            category: List<Category>?,
            level: List<String>?,
            sortBy: String?
        )
    }

    fun setFilterListener(listener: OnFilterListener) {
        filterListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
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
        }
        binding.tvResetFilter.setOnClickListener {
            resetFilter()
            dismiss()
        }
    }

    private fun resetFilter() {
        val searchQuery = null
        val selectedType = null
        val selectedCategories = emptyList<Category>()
        viewModel.clearSelectedCategories()

        val selectedLevels = mutableListOf<String>().apply {
            isBeginnerChecked = false
            isIntermediateChecked = false
            isAdvancedChecked = false

            binding.cbBeginner.isChecked = isBeginnerChecked
            binding.cbIntermediate.isChecked = isIntermediateChecked
            binding.cbAdvance.isChecked = isAdvancedChecked
        }
        val selectedSortBy = null
        binding.topPicks.clearCheck()

        filterListener?.onFilterApplied(searchQuery, selectedType, selectedCategories, selectedLevels, selectedSortBy)
    }

    private fun applyFilter() {
        val searchQuery = viewModel.searchQuery.value
        val selectedType = viewModel.selectedType.value
        val selectedCategories = viewModel.selectedCategories.value
        val selectedLevels = mutableListOf<String>().apply {
            if (binding.cbBeginner.isChecked) add(BEGINNER_LEVEL)
            if (binding.cbIntermediate.isChecked) add(INTERMEDIATE_LEVEL)
            if (binding.cbAdvance.isChecked) add(ADVANCED_LEVEL)
        }
        val selectedSortBy = when (binding.topPicks.checkedRadioButtonId) {
            R.id.rb_new -> NEW_SORT
            R.id.rb_popular -> POPULAR_SORT
            R.id.rb_promo -> PROMO_SORT
            else -> null
        }
        filterListener?.onFilterApplied(searchQuery, selectedType, selectedCategories, selectedLevels, selectedSortBy)
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
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

    companion object {
        const val BEGINNER_LEVEL = "beginner"
        const val INTERMEDIATE_LEVEL = "intermediate"
        const val ADVANCED_LEVEL = "advanced"
        const val NEW_SORT = "terbaru"
        const val POPULAR_SORT = "terpopuler"
        const val PROMO_SORT = "promo"
    }
}
