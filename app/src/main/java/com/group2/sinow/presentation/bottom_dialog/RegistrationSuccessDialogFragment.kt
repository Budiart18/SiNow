package com.group2.sinow.presentation.bottom_dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.group2.sinow.MainActivity
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentRegistrationSuccessDialogBinding

class RegistrationSuccessDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRegistrationSuccessDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationSuccessDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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