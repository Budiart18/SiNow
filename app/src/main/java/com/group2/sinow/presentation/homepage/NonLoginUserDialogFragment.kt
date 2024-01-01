package com.group2.sinow.presentation.homepage

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.group2.sinow.databinding.FragmentNonLoginUserDialogBinding
import com.group2.sinow.presentation.auth.login.LoginActivity

class NonLoginUserDialogFragment : SuperBottomSheetFragment() {

    private lateinit var binding: FragmentNonLoginUserDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentNonLoginUserDialogBinding.inflate(inflater, container, false)
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
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnLogin.setOnClickListener {
            navigateToLogin()
        }
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}
