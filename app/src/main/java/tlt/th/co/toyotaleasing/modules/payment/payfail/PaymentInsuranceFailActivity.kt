package tlt.th.co.toyotaleasing.modules.payment.payfail

import android.content.Context
import android.content.Intent
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.payfail.common.PaymentFailActivity

class PaymentInsuranceFailActivity : PaymentFailActivity() {
    override fun onHomeClicked() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSURANCE_MENU_POSITION)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PaymentInsuranceFailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
