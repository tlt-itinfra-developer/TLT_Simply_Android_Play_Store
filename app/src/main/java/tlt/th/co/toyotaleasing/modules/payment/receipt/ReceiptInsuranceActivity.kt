package tlt.th.co.toyotaleasing.modules.payment.receipt

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_receipt.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.common.ReceiptActivity

open class ReceiptInsuranceActivity : ReceiptActivity() {

    override fun initialData() {
        isDelay = false
        isDelay.ifTrue {
            receiptData = CheckStatusFromBankResponse()
        }
    }

    override fun onPaymentSuccess() {
        txt_payment_type.text = getString(R.string.receipt_payment_type_insurance)
        description.text = getString(R.string.receipt_description_insurance_paid_success)
    }

    override fun onBackToHomeClick() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSURANCE_MENU_POSITION)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, ReceiptInsuranceActivity::class.java)
            context?.startActivity(intent)
        }
    }
}