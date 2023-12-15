package com.group2.sinow.presentation.auth.otp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.databinding.ActivityOtpBinding
import com.group2.sinow.presentation.bottom_dialog.RegistrationSuccessDialogFragment
import com.group2.sinow.presentation.main.MainActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class OTPActivity : AppCompatActivity() {

    private val viewModel: OTPViewModel by viewModel()

    private val binding: ActivityOtpBinding by lazy {
        ActivityOtpBinding.inflate(layoutInflater)
    }

    private var timer: CountDownTimer? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        startCountdownTimer()
        setupListeners()
        displayEmail()
    }

    private fun startCountdownTimer() {
        timer?.cancel() // Cancel any existing timer
        timer = object : CountDownTimer(60000, 1000) { // 60 seconds, 1 second tick
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / 1000
                val countdownText = "Kirim Ulang OTP dalam "
                val timeLeftText = "$timeLeft detik"
                val fullText = countdownText + timeLeftText

                val spannableString = SpannableStringBuilder(fullText).apply {
                    setSpan(
                        ForegroundColorSpan(Color.parseColor("#6148FF")),
                        countdownText.length,
                        fullText.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }

                binding.tvCountdownTimer.text = spannableString
            }

            override fun onFinish() {
                val finishText = "Kirim Ulang"
                val spannableString = SpannableStringBuilder(finishText).apply {
                    setSpan(
                        ForegroundColorSpan(Color.RED),
                        0,
                        finishText.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        finishText.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }

                binding.tvCountdownTimer.text = spannableString
                binding.tvResendOtp.isVisible = true
            }
        }.start()
    }



    private fun displayEmail() {
        val email = intent.getStringExtra("email") ?: ""
        binding.tvEmail.text = email
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvResendOtp.setOnClickListener {
            val email = intent.getStringExtra("email")
            if (email != null) {
                viewModel.resendOtp(email)
                startCountdownTimer()
            } else {
                Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSendOtp.setOnClickListener {
            val otpCode = binding.pinview.text.toString()
            if (otpCode.length == 6) {
                val email = intent.getStringExtra("email")
                val verifyEmailRequest = VerifyEmailRequest(email!!, otpCode)
                viewModel.verifyEmail(verifyEmailRequest)
            } else {
                Toast.makeText(this, "wrong", Toast.LENGTH_SHORT).show()

            }
        }



        observeViewModel()
    }



    private fun observeViewModel() {
        viewModel.verificationState.observe(this){ resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = false
                    it.payload?.data?.token.let { token ->
                        if (token != null) {
                            viewModel.saveToken(token)
                        }
                    }
                    showRegistrationSuccessDialog()
                }, doOnLoading = {
                    binding.progressBar.isVisible = true
                    binding.tvErrorMessage.isVisible = false
                }, doOnError = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = true
                    if (it.exception is ApiException){
                        binding.tvErrorMessage.text = it.exception.getParsedError()?.message
                    }


                })
        }

        viewModel.resendOtpStatus.observe(this){result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = false

                },
                doOnLoading = {
                    binding.progressBar.isVisible = true
                    binding.tvErrorMessage.isVisible = false
                },
                doOnError = {
                    binding.progressBar.isVisible = false
                    binding.tvErrorMessage.isVisible = true
                    if (it.exception is ApiException){
                        binding.tvErrorMessage.text = it.exception.getParsedError()?.message
                    }
                }
            )
        }
    }

    private fun showRegistrationSuccessDialog() {
        val dialog = RegistrationSuccessDialogFragment()
        dialog.show(supportFragmentManager, "RegistrationSuccessDialogFragment")
    }


    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    }
