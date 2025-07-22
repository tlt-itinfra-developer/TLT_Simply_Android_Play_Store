package tlt.th.co.toyotaleasing.modules.payment.receipt

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_receipt.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.common.ReceiptActivity

open class ReceiptInstallmentActivity : ReceiptActivity() {

    override fun initialData() {
        isDelay = false
        isDelay.ifTrue {
            receiptData = CheckStatusFromBankResponse()
        }
    }

    override fun onPaymentSuccess() {
        receipt_header.text = getString(R.string.receipt_payment_installment_success)
        txt_payment_type.text = getString(R.string.receipt_payment_type_installment)
        description.visible()
        description.text = getString(R.string.receipt_description_insatllment_paid_success)
    }

    override fun onBackToHomeClick() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSTALLMENT_MENU_POSITION)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, ReceiptInstallmentActivity::class.java)
            context?.startActivity(intent)
        }
    }
}