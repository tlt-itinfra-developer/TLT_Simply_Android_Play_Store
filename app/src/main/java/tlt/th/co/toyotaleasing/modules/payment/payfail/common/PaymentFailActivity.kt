package tlt.th.co.toyotaleasing.modules.payment.payfail.common

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_payment_fail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity

abstract class PaymentFailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_fail)

        initInstances()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.PAYMENT_FAIL)
    }

    //override fun onBackPressedSupport() {}

    private fun initInstances() {
        btn_home.setOnClickListener {
            AnalyticsManager.paymentFailMainPage()
            onHomeClicked()
        }
    }

    abstract fun onHomeClicked()
}
