package tlt.th.co.toyotaleasing.modules.verifyemail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_verify_email.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class VerifyEmailActivity : BaseActivity() {

    private val THREE_SECOND = 1000L
    private val handlerAutoVerifyEmail = Handler()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(VerifyEmailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)

        initViewModel()
        initInstances()
    }

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
            VerifyEmailSuccessActivity.open(this@VerifyEmailActivity)
            finish()
        })

        viewModel.whenResendMailFailure.observe(this, Observer {
            Toast.makeText(this, "Resend Fail", Toast.LENGTH_SHORT).show()
        })

        viewModel.whenResendMailSuccess.observe(this, Observer {
            Toast.makeText(this, "Resend Success", Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
        viewModel.setEmail("")

        btn_confirm.setOnClickListener {
            ExternalAppUtils.openMailApp(this)
        }

        btn_resend_email.setOnClickListener {
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
        private const val EMAIL_EXTRA = "EMAIL"

        fun open(context: Context?, email: String) {
            val intent = Intent(context, VerifyEmailActivity::class.java)
            intent.putExtra(EMAIL_EXTRA, email)
            context?.startActivity(intent)
        }
    }
}
