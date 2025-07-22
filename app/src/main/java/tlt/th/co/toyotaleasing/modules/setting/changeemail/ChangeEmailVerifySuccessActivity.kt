package tlt.th.co.toyotaleasing.modules.setting.changeemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_change_email_verify_success.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.main.MainActivity

class ChangeEmailVerifySuccessActivity : BaseActivity() {

    private val userEmail by lazy {
        intent.getStringExtra(EMAIL_EXTRA) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email_verify_success)

        initInstances()
    }

    private fun initInstances() {
        email_user.text = userEmail

        button.setOnClickListener {
            AnalyticsManager.settingChangeEmailSuccessButton()
            MainActivity.openWithClearStack(this@ChangeEmailVerifySuccessActivity)
        }
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.SETTING_CHANGE_EMAIL_SUCCESS)
    }

    companion object {
        const val EMAIL_EXTRA = "profileuseremail"

        fun start(context: Context?, email: String) {
            val intent = Intent(context, ChangeEmailVerifySuccessActivity::class.java)
            intent.putExtra(EMAIL_EXTRA, email)
            context?.startActivity(intent)
        }
    }
}
