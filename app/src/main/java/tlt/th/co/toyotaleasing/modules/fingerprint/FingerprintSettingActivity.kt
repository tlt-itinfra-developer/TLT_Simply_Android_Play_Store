package tlt.th.co.toyotaleasing.modules.fingerprint

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_fingerprint_setting.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.model.request.SetTouchIDRequest
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.pincode.IsExitDialogFragment

class FingerprintSettingActivity : BaseActivity(),
        IsExitDialogFragment.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FingerprintSettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fingerprint_setting)

        initViewModel()
        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_SET_PIN_SUCCESS)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenSetupFingerprintSuccess.observe(this, Observer {
            MainCustomerActivity.startWithClearStack(this@FingerprintSettingActivity)
        })

        viewModel.whenSetupFingerprintFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            AnalyticsManager.pinSuccessSetTouchIdClicked()
            FingerprintSettingDialogFragment.show(supportFragmentManager,
                    object : FingerprintSettingDialogFragment.Listener {
                        override fun onFingerprintSuccess() {
                            viewModel.setupFingerprintAuth(SetTouchIDRequest.buildForActivate())
                        }
                    })
        }

        btn_fingerprint_setting_skip.setOnClickListener {
            AnalyticsManager.pinSuccessGoMainClicked()
            viewModel.setupFingerprintAuth(SetTouchIDRequest.buildForDeactivate())
        }
    }

    override fun onIsExitCancelClicked() {
    }

    override fun onIsExitConfirmClicked() {
        finishAffinity()
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, FingerprintSettingActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }
    }
}
