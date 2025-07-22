package tlt.th.co.toyotaleasing.modules.payment.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_checkout.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.ibanking.IBankingActivity
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutActivity
import tlt.th.co.toyotaleasing.modules.payment.payfail.PaymentInstallmentFailActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptInsuranceActivity
import tlt.th.co.toyotaleasing.modules.qrcode.detail.QRActivity

open class CheckoutInsuranceActivity : CheckoutActivity() {

    override fun setImageList() {
        rv_payment_file_document_information.gone()
    }

    override fun getCheckoutType(): String {
        return CheckoutActivity.INSURANCE_TYPE
    }

    override fun onViewReady() {
        viewModel.getPayment()
    }

    override fun onPaymentTitleChange(): String {
        return getString(R.string.payment_insurance)
    }

    override fun onBarcodeClick() {
        QRActivity.start(this,
                QRActivity.CHECKOUT_REQUEST_CODE,
                QRActivity.TransactionType.INSURANCE
        )
    }

    override fun onPaymentClick() {
        IBankingActivity.startWithResult(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_OK) {
            ReceiptInsuranceActivity.start(this)
            return
        }

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_CANCELED) {
            MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSURANCE_MENU_POSITION)
            return
        }

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == IBankingActivity.PAY_FAIL) {
            viewModel.checkStatusFromBankBySequenceId()
            return
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CheckoutInsuranceActivity::class.java)
            context.startActivity(intent)
        }
    }
}