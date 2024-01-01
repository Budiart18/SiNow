package com.group2.sinow.presentation.bottomdialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentStartLearningDialogBinding
import com.group2.sinow.presentation.detail.DetailCourseViewModel
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class StartLearningDialogFragment : SuperBottomSheetFragment() {
    private lateinit var binding: FragmentStartLearningDialogBinding

    private val sharedViewModel: DetailCourseViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentStartLearningDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getExpandedHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        println(screenHeight)
        return (screenHeight * 0.6).toInt()
    }

    override fun isSheetAlwaysExpanded(): Boolean = true

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        observeCourseData()
        observeIsFollowingCourse()
    }

    private fun setClickListener() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun observeCourseData() {
        sharedViewModel.detailCourseData.observe(viewLifecycleOwner) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.btnJoinClass.setOnClickListener { _ ->
                        followCourse(it.payload?.courseId)
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.loadingAnimation.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.loadingAnimation.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    if (it.exception is ApiException) {
                        binding.layoutState.tvError.text =
                            it.exception.getParsedError()?.message.orEmpty()
                    }
                }
            )
        }
    }

    private fun observeIsFollowingCourse() {
        sharedViewModel.isFollowingCourse.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.tv_toast_success_follow_class),
                        Toast.LENGTH_SHORT
                    ).show()
                    dismiss()
                },
                doOnError = {
                    if (it.exception is ApiException) {
                        Toast.makeText(
                            requireContext(),
                            it.exception.getParsedError()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    private fun followCourse(courseId: Int?) {
        sharedViewModel.followCourse(courseId)
    }
}
