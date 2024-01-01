package com.group2.sinow.presentation.bottomdialog

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.load
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentBuyPremiumCourseDialogBinding
import com.group2.sinow.model.detailcourse.CourseData
import com.group2.sinow.presentation.detail.DetailCourseViewModel
import com.group2.sinow.presentation.payment.PaymentActivity
import com.group2.sinow.presentation.transactionhistory.TransactionHistoryActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.group2.sinow.utils.toCurrencyFormat
import com.shashank.sony.fancytoastlib.FancyToast
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BuyPremiumCourseDialogFragment : SuperBottomSheetFragment() {
    private lateinit var binding: FragmentBuyPremiumCourseDialogBinding

    private val sharedViewModel: DetailCourseViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentBuyPremiumCourseDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getExpandedHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
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
        observeBuyCourseResult()
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
                    bindDetailCourse(it.payload)
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

    private fun bindDetailCourse(courseData: CourseData?) {
        courseData?.let { item ->
            binding.courseCard.ivCourseImage.load(item.course?.imageUrl)
            binding.courseCard.tvCourseCategory.text = item.course?.category?.name
            binding.courseCard.tvCourseName.text = item.course?.name
            binding.courseCard.tvCourseRate.text = item.course?.rating.toString()
            binding.courseCard.tvCourseAuthor.text =
                getString(
                    R.string.format_course_by,
                    item.course?.courseBy
                )
            binding.courseCard.tvCourseLevel.text =
                item.course?.level?.replaceFirstChar {
                    it.uppercase()
                }
            binding.courseCard.tvCourseModules.text =
                getString(
                    R.string.format_course_module,
                    item.course?.totalModule
                )
            binding.courseCard.tvCourseDuration.text =
                getString(
                    R.string.format_course_duration,
                    item.course?.totalDuration
                )
            binding.courseCard.tvBtnBuy.text = item.course?.price?.toDouble()?.toCurrencyFormat()
            binding.courseCard.ivPremium.isVisible = true
            binding.btnBuyNow.setOnClickListener {
                buyPremiumCourse(item.courseId)
            }
        }
    }

    private fun buyPremiumCourse(courseId: Int?) {
        sharedViewModel.buyPremiumCourse(courseId)
    }

    private fun observeBuyCourseResult() {
        sharedViewModel.buyPremiumCourseResult.observe(viewLifecycleOwner) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    navigateToPayment(it.payload?.redirectUrl)
                },
                doOnError = {
                    if (it.exception is ApiException) {
                        FancyToast.makeText(
                            requireContext(),
                            it.exception.getParsedError()?.message.orEmpty(),
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                        if (it.exception.httpCode == 400) {
                            navigateTolPaymentHistory()
                        }
                    }
                }
            )
        }
    }

    private fun navigateTolPaymentHistory() {
        val intent = Intent(requireContext(), TransactionHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPayment(redirectUrl: String?) {
        val intent =
            Intent(requireContext(), PaymentActivity::class.java).apply {
                putExtra(URL, redirectUrl)
            }
        startActivity(intent)
    }

    companion object {
        const val URL = "URL"
    }
}
