package tlt.th.co.toyotaleasing.modules.payment.receipt

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_receipt.*
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.common.ReceiptActivity

open class ReceiptDelayActivity : ReceiptActivity() {

    override fun initialData() {
        toolbar.showBackLeftButton()
        toolbar.setOnHambergerMenuClickListener {
            finish()
        }
        isDelay = intent.getBooleanExtra(IS_DELAY, false)
    }

    override fun onPaymentSuccess() {

    }

    override fun onBackToHomeClick() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSTALLMENT_MENU_POSITION)
    }

    companion object {
        const val IS_DELAY = "isdelay"
        const val CHECK_STATUS_FROM_BANK_DATA = "checkStatusFromBankData"

        fun start(context: Context?, isDelay: Boolean = false, item: CheckStatusFromBankResponse) {
            val intent = Intent(context, ReceiptDelayActivity::class.java).apply {
                putExtra(IS_DELAY, isDelay)
                putExtra(CHECK_STATUS_FROM_BANK_DATA, item)
            }
            context?.startActivity(intent)
        }
    }
}