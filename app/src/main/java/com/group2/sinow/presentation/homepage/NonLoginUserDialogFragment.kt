package com.group2.sinow.presentation.homepage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group2.sinow.MainActivity
import com.group2.sinow.databinding.FragmentNonLoginUserDialogBinding

class NonLoginUserDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNonLoginUserDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNonLoginUserDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

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
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

}