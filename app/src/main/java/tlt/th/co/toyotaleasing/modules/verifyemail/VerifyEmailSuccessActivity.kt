package tlt.th.co.toyotaleasing.modules.verifyemail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_verify_email.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity

class VerifyEmailSuccessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email_success)

        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.VERIFY_EMAIL_SUCCESS)
    }

    private fun initInstances() {
        btn_confirm.setOnClickListener {
            AnalyticsManager.verifyEmailSuccessNext()
            finish()
//            TermAndConditionActivity.open(this@VerifyEmailSuccessActivity)

        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, VerifyEmailSuccessActivity::class.java)
            context.startActivity(intent)
        }
    }
}
