package com.group2.sinow.presentation.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.databinding.FragmentAccountBinding
import com.group2.sinow.presentation.auth.login.LoginActivity
import com.group2.sinow.presentation.auth.register.RegisterActivity
import com.group2.sinow.presentation.change_password.ChangePasswordActivity
import com.group2.sinow.presentation.payment_history.PaymentHistoryActivity
import com.group2.sinow.presentation.profile.ProfileActivity
import com.group2.sinow.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        getData()
        observeData()
    }

    private fun observeData() {
        viewModel.userData.observe(viewLifecycleOwner){
            if (it.payload != null) {
                binding.cvAccount.isVisible = true
                binding.layoutNonloginAccount.root.isVisible = false
            } else {
                binding.cvAccount.isVisible = false
                binding.layoutNonloginAccount.root.isVisible = true
            }
        }
    }

    private fun getData() {
        viewModel.getUserData()
    }

    private fun setClickListener() {
        binding.tvProfileTitle.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }
        binding.tvChangePasswordTitle.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
        }
        binding.tvPaymentHistoryTitle.setOnClickListener {
            startActivity(Intent(requireContext(), PaymentHistoryActivity::class.java))
        }
        binding.tvLogoutTitle.setOnClickListener {

        }
        binding.layoutNonloginAccount.btnLogin.setOnClickListener {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
        binding.layoutNonloginAccount.btnRegister.setOnClickListener {
            startActivity(Intent(requireContext(), RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}