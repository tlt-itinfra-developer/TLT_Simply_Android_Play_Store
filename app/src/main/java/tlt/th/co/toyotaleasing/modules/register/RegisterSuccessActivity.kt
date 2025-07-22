package tlt.th.co.toyotaleasing.modules.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register_success.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.modules.pincode.SetupPincodeActivity

class RegisterSuccessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_success)

        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.REGISTER_SUCCESS)
    }

//    override fun onBackPressedSupport() {
//        Toast.makeText(this, getString(R.string.error_cant_back), Toast.LENGTH_SHORT).show()
//    }

    private fun initInstances() {
        btn_register_success.setOnClickListener {
            AnalyticsManager.registerSuccessClicked()
            SetupPincodeActivity.start(this@RegisterSuccessActivity)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterSuccessActivity::class.java)
            context.startActivity(intent)
        }
    }
}
