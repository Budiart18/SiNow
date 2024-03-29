package com.group2.sinow.presentation.bottomdialog

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.databinding.FragmentRegistrationSuccessDialogBinding
import com.group2.sinow.presentation.main.MainActivity

class RegistrationSuccessDialogFragment : SuperBottomSheetFragment() {
    private lateinit var binding: FragmentRegistrationSuccessDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegistrationSuccessDialogBinding.inflate(inflater, container, false)
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
    }

    private fun setClickListener() {
        binding.btnGoToHome.setOnClickListener {
            navigateToHome()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }
}
