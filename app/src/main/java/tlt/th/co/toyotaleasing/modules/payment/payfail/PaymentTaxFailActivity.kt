package tlt.th.co.toyotaleasing.modules.payment.payfail

import android.content.Context
import android.content.Intent
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.payfail.common.PaymentFailActivity

class PaymentTaxFailActivity : PaymentFailActivity() {
    override fun onHomeClicked() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.TAX_MENU_POSITION)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PaymentTaxFailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
