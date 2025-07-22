package tlt.th.co.toyotaleasing.modules.payment.payfail

import android.content.Context
import android.content.Intent
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.payfail.common.PaymentFailActivity

class PaymentInstallmentFailActivity : PaymentFailActivity() {
    override fun onHomeClicked() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSTALLMENT_MENU_POSITION)
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, PaymentInstallmentFailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
