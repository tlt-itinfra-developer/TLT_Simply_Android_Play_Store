package tlt.th.co.toyotaleasing.modules.verifyemail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_verify_email_fail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class VerifyEmailFailActivity : BaseActivity() {

    private val THREE_SECOND = 3000L
    private val handlerAutoVerifyEmail = Handler()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(VerifyFailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email_fail)

        initViewModel()
        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.VERIFY_EMAIL_FAIL)
    }

    //override fun onBackPressedSupport() {}

    override fun onResume() {
        super.onResume()
        handlerAutoVerifyEmail.post(autoVerifyEmailEvery3sec)
    }

    override fun onStop() {
        handlerAutoVerifyEmail.removeCallbacks(autoVerifyEmailEvery3sec)
        super.onStop()
    }

    private fun initViewModel() {
        viewModel.whenGetDefaultEmail.observe(this, Observer {
            txt_email.text = it
        })

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenVerifyEmailSuccess.observe(this, Observer {
            VerifyEmailSuccessActivity.open(this@VerifyEmailFailActivity)
        })

        viewModel.whenResendEmailFailure.observe(this, Observer {
            Toast.makeText(this, "Resend Fail", Toast.LENGTH_SHORT).show()
        })

        viewModel.whenResendEmailSuccess.observe(this, Observer {
            Toast.makeText(this, "Resend Success", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
        viewModel.init()

        btn_confirm.setOnClickListener {
            AnalyticsManager.verifyEmailFailConfirm()
            ExternalAppUtils.openMailApp(this)
        }

        btn_resend_email.setOnClickListener {
            AnalyticsManager.verifyEmailFailResend()
            viewModel.resendEmail()
        }
    }

    private val autoVerifyEmailEvery3sec = object : Runnable {
        override fun run() {
            viewModel.checkVerifyEmail()
            handlerAutoVerifyEmail.postDelayed(this, THREE_SECOND)
        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, VerifyEmailFailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
