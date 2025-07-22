package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.github.ajalt.reprint.core.Reprint
import kotlinx.android.synthetic.main.activity_setup_pincode.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.fingerprint.FingerprintSettingActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity

class SetupPincodeActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SetupPincodeViewModel::class.java)
    }

    private val oldPincode by lazy {
        intent?.getStringExtra(OLD_PINCODE_EXTRA) ?: ""
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_SET_PIN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_pincode)

        initViewModel()
        initInstances()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenValidatePasswordSuccess.observe(this, Observer {
            val pincode = input_create_pin.text.toString()

            if (viewModel.isResetPincodeFlow()) {
                viewModel.changePincode(oldPincode, pincode)
            } else {
                viewModel.sendPincodeToApi(pincode)
            }
        })

        viewModel.whenPasswordNotMatchMessage.observe(this, Observer {
            val message = getString(R.string.error_password_not_match)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenPasswordRequire6DigitsMessage.observe(this, Observer {
            val message = getString(R.string.error_password_require_6_digits)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenValidatePasswordFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSetupPincodeFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenVerifyPatternPincodeFail.observe(this, Observer {
            val message = getString(R.string.error_password_pattern)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenSetupPincodeSuccess.observe(this, Observer {
            if (viewModel.isRegisterFlow()
                    && Reprint.isHardwarePresent()
                    && Reprint.hasFingerprintRegistered()) {
                FingerprintSettingActivity.open(this@SetupPincodeActivity)
            } else if (viewModel.isRegisterFlow()) {
                MainCustomerActivity.startWithClearStack(this@SetupPincodeActivity)
            } else {
                ChangePincodeSuccessActivity.start(this@SetupPincodeActivity)
            }
        })
    }

    private fun initInstances() {
        textinput_password.isPasswordVisibilityToggleEnabled.ifTrue {
            AnalyticsManager.setViewPinCodeClicked()
        }

        textinput_password.setOnClickListener { AnalyticsManager.setPinCodeClicked() }
        textinput_confirm_password.setOnClickListener { AnalyticsManager.setConfirmPinCodeClicked() }

        btn_create_pin_success.setOnClickListener {
            AnalyticsManager.setPinCodeConfirmButtonClicked()
            val password = input_create_pin.text.toString().trim()
            val confirmPassword = input_create_pin_confirm.text.toString().trim()

            viewModel.validatePassword(password, confirmPassword)
        }
    }

    companion object {
        const val OLD_PINCODE_EXTRA = "OLD_PINCODE_EXTRA"

        fun start(context: Context?, oldPincode: String = "") {
            val intent = Intent(context, SetupPincodeActivity::class.java).apply {
                putExtra(OLD_PINCODE_EXTRA, oldPincode)
            }

            context?.startActivity(intent)
        }
    }
}
