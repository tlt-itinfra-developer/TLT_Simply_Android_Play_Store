package tlt.th.co.toyotaleasing.modules.otp

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.core.content.ContextCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_otp.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.invisible
import tlt.th.co.toyotaleasing.common.extension.toMinsAndSecs
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.pincode.SetupPincodeActivity
import tlt.th.co.toyotaleasing.modules.register.RegisterSuccessActivity
import tlt.th.co.toyotaleasing.view.KeyboardNumberWidget
import tlt.th.co.toyotaleasing.view.OTPEdittext

class OTPActivity : BaseActivity() {

    private lateinit var countDownTimer: CountDownTimer

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(OTPViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        initViewModel()
        initInstances()

        waitingForResendOTP()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_FILL_OTP)
        super.onStop()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenSendOTPSuccess.observe(this, Observer {

        })

        viewModel.whenSendOTPFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenVerifyOTPSuccess.observe(this, Observer {
            if (viewModel.isRegisterFlow()) {
                RegisterSuccessActivity.start(this@OTPActivity)
            } else {
                SetupPincodeActivity.start(this@OTPActivity)
            }
        })

        viewModel.whenVerifyOTPFailure.observe(this, Observer {
            AnalyticsManager.trackScreenError(AnalyticsScreenName.REGISTER_FILL_OTP_ERROR)
            enableOTPError(it!!)
        })
    }

    private fun initInstances() {
        keyboard_number_view.setListener(object : KeyboardNumberWidget.Listener {
            override fun onNumberClick(number: String) {
                edittext_otp.addNumber(number)
            }

            override fun onDeleteClick() {
                edittext_otp.deleteNumber()
            }
        })

        edittext_otp.setListener(object : OTPEdittext.Listener {
            override fun onOTPComplete(otp: String) {
                viewModel.verifyOTP(otp)
            }
        })

        edittext_otp.setOnClickListener {
            AnalyticsManager.otpCode()
        }

        btn_resend_otp.setOnClickListener {
            AnalyticsManager.otpResendCode()
            disableOTPError()
            waitingForResendOTP()
        }
    }

    private fun waitingForResendOTP() {
        viewModel.sendOTP()

        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countDownTimer = object : CountDownTimer(viewModel.otpCountDownTimer, 1000) {
            override fun onFinish() {
                txt_resend_time.text = "00:00"
                AnalyticsManager.trackScreenError(AnalyticsScreenName.REGISTER_FILL_OTP_TIMEOUT)
                enableOTPError(viewModel.otpTimeoutMessage)
            }

            override fun onTick(millisUntilFinished: Long) {
                txt_resend_time.text = millisUntilFinished.toMinsAndSecs()
            }
        }
        countDownTimer.start()
    }

    private fun enableOTPError(message: String) {
        txt_error.visible()
        txt_error.text = message
        otp_underline.setBackgroundColor(ContextCompat.getColor(this@OTPActivity, R.color.cherry_red))
    }

    private fun disableOTPError() {
        txt_error.invisible()
        otp_underline.setBackgroundColor(ContextCompat.getColor(this@OTPActivity, R.color.unactive))
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, OTPActivity::class.java)
            context.startActivity(intent)
        }
    }
}
