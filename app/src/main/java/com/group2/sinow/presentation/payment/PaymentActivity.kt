package com.group2.sinow.presentation.payment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.group2.sinow.databinding.ActivityPaymentBinding

class PaymentActivity : AppCompatActivity() {

    private val binding: ActivityPaymentBinding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val url = intent.getStringExtra("URL")
        openUrlFromWebView(url)
        setupListener()
    }

    private fun setupListener() {
        binding.btnCloseSnap.setOnClickListener {
            onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun openUrlFromWebView(url: String?) {
        val webView = binding.myWebView
        webView.webViewClient = object : WebViewClient() {
            val pd = ProgressDialog(this@PaymentActivity)

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val requestUrl = request?.url.toString()
                if (requestUrl.contains("gojek://") ||
                    requestUrl.contains("shopeeid://") ||
                    requestUrl.contains("//wsa.wallet.airpay.co.id/") ||
                    requestUrl.contains("/gopay/partner/") ||
                    requestUrl.contains("/shopeepay/")
                ) {
                    val intent = Intent(Intent.ACTION_VIEW, request?.url)
                    startActivity(intent)
                    return true
                } else {
                    return false
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                pd.setMessage("loading")
                pd.show()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                pd.hide()
                super.onPageFinished(view, url)
            }
        }
        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webView.loadUrl(url.toString())
        binding.swipeRefresh.setOnRefreshListener {
            webView.loadUrl(url.toString())
            binding.swipeRefresh.isRefreshing = false
        }
    }
}
