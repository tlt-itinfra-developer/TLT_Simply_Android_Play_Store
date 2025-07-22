package tlt.th.co.toyotaleasing.modules.payment.receipt

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_receipt.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.common.ReceiptActivity

open class ReceiptTaxActivity : ReceiptActivity() {

    override fun initialData() {
        isDelay = false
        isDelay.ifTrue {
            receiptData = CheckStatusFromBankResponse()
        }
    }

    override fun onPaymentSuccess() {
        receipt_header.text = getString(R.string.receipt_payment_tax_success)
        txt_payment_type.text = getString(R.string.receipt_payment_type_tax)
        description.gone()
    }

    override fun onBackToHomeClick() {
        MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.TAX_MENU_POSITION)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, ReceiptTaxActivity::class.java)
            context?.startActivity(intent)
        }
    }
}