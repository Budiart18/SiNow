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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.group2.sinow.R
import com.group2.sinow.data.network.api.model.verifyemail.VerifyEmailRequest
import com.group2.sinow.databinding.ActivityOtpBinding
import com.group2.sinow.presentation.bottom_dialog.RegistrationSuccessDialogFragment
import com.group2.sinow.presentation.main.MainActivity
import com.group2.sinow.utils.exceptions.ApiException
import com.group2.sinow.utils.proceedWhen
import com.shashank.sony.fancytoastlib.FancyToast
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
                val countdownText = getString(R.string.text_repeat_send_otp_in)
                val timeLeftText = getString(R.string.second, timeLeft)
                val fullText = countdownText + timeLeftText

                val spannableString = SpannableStringBuilder(fullText).apply {
                    setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this@OTPActivity, R.color.app_color_primary)),
                        countdownText.length,
                        fullText.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                }
                binding.tvCountdownTimer.text = spannableString
            }

            override fun onFinish() {
                val finishText = getString(R.string.text_repeat_send)
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
                FancyToast.makeText(
                    this,
                    getString(R.string.text_email_not_valid),
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }

        binding.btnSendOtp.setOnClickListener {
            val otpCode = binding.pinview.text.toString()
            if (otpCode.length == 6) {
                val email = intent.getStringExtra("email")
                val verifyEmailRequest = VerifyEmailRequest(email!!, otpCode)
                viewModel.verifyEmail(verifyEmailRequest)
            } else {
                FancyToast.makeText(
                    this,
                    getString(R.string.text_otp_code_wrong),
                    FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.verificationState.observe(this) { resultWrapper ->
            resultWrapper.proceedWhen(
                doOnSuccess = {
                    binding.btnSendOtp.isVisible = true
                    binding.progressBar.isVisible = false
                    it.payload?.data?.token.let { token ->
                        if (token != null) {
                            viewModel.saveToken(token)
                        }
                    }
                    showRegistrationSuccessDialog()
                }, doOnLoading = {
                    binding.btnSendOtp.isVisible = false
                    binding.progressBar.isVisible = true
                }, doOnError = {
                    binding.btnSendOtp.isVisible = true
                    binding.progressBar.isVisible = false
                    if (it.exception is ApiException) {
                        FancyToast.makeText(
                            this,
                            it.exception.getParsedError()?.message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
                    }
                }
            )
        }

        viewModel.resendOtpStatus.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.btnSendOtp.isVisible = true
                    binding.progressBar.isVisible = false
                },
                doOnLoading = {
                    binding.btnSendOtp.isVisible = false
                    binding.progressBar.isVisible = true
                },
                doOnError = {
                    binding.btnSendOtp.isVisible = true
                    binding.progressBar.isVisible = false
                    if (it.exception is ApiException) {
                        FancyToast.makeText(
                            this,
                            it.exception.getParsedError()?.message,
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            false
                        ).show()
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