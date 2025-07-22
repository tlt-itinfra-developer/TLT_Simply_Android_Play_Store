package tlt.th.co.toyotaleasing.modules.payment.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_checkout.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.ibanking.IBankingActivity
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutActivity
import tlt.th.co.toyotaleasing.modules.payment.payfail.PaymentInstallmentFailActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptInstallmentActivity
import tlt.th.co.toyotaleasing.modules.qrcode.detail.QRActivity

open class CheckoutInstallmentActivity : CheckoutActivity() {

    override fun setImageList() {
        rv_payment_file_document_information.gone()
    }

    override fun getCheckoutType(): String {
        return CheckoutActivity.INSTALLMENT_TYPE
    }

    override fun onViewReady() {
        txt_payment_title.text = getString(R.string.payment_summary_list)
        viewModel.getPayment()
    }

    override fun onPaymentTitleChange(): String {
        return getString(R.string.payment_summary_list)
    }

    override fun onBarcodeClick() {
        QRActivity.start(this,
                QRActivity.CHECKOUT_REQUEST_CODE,
                QRActivity.TransactionType.INSTALLMENT
        )
    }

    override fun onPaymentClick() {
        IBankingActivity.startWithResult(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_OK) {
            ReceiptInstallmentActivity.start(this)
            return
        }

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_CANCELED) {
            MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.INSTALLMENT_MENU_POSITION)
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
            val intent = Intent(context, CheckoutInstallmentActivity::class.java)
            context.startActivity(intent)
        }
    }
}