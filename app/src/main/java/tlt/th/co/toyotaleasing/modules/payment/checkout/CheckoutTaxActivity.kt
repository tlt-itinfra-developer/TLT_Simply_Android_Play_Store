package tlt.th.co.toyotaleasing.modules.payment.checkout

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import kotlinx.android.synthetic.main.activity_checkout.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.ibanking.IBankingActivity
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutActivity
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptTaxActivity
import tlt.th.co.toyotaleasing.modules.qrcode.detail.QRActivity
import tlt.th.co.toyotaleasing.modules.tax.ImageUploadViewModel

open class CheckoutTaxActivity : CheckoutActivity() {

    override fun setImageList() {
        rv_payment_file_document_information.visible()
    }

    override fun getCheckoutType(): String {
        return CheckoutActivity.TAX_TYPE
    }

    private val imageUploadViewModel by lazy {
        ViewModelProviders.of(this).get(ImageUploadViewModel::class.java)
    }

    private val isShowFileAttach by lazy {
        intent.getIntExtra(REQUEST_CODE_EXTRA, DEFAULT_REQUEST_CODE) == DEFAULT_REQUEST_CODE
    }

    override fun onViewReady() {
        viewModel.getPayment(isShowFileAttach)
    }

    override fun onPaymentTitleChange(): String {
        return getString(R.string.payment_summary_list)
    }

    override fun onBarcodeClick() {
        QRActivity.start(this,
                QRActivity.CHECKOUT_REQUEST_CODE,
                QRActivity.TransactionType.TAX
        )
    }

    override fun onPaymentClick() {
        PaymentManager.deletePorlorborDocsState()
        PaymentManager.deleteTorloraorDocsState()
        IBankingActivity.startWithResult(this)

        imageUploadViewModel.apply {
            uploadByPorlorbor()
            uploadByTorloraor()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_OK) {
            ReceiptTaxActivity.start(this)
            return
        }

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == Activity.RESULT_CANCELED) {
            MainCustomerActivity.startWithClearStack(this, MainCustomerActivity.TAX_MENU_POSITION)
            return
        }

        if (requestCode == IBankingActivity.IBANKING
                && resultCode == IBankingActivity.PAY_FAIL) {
            viewModel.checkStatusFromBankBySequenceId()
            return
        }
    }

    companion object {
        const val REQUEST_CODE_EXTRA = "REQUEST_CODE_EXTRA"

        const val DEFAULT_REQUEST_CODE = 200

        fun startWithResult(activity: Activity,
                            requestCode: Int = DEFAULT_REQUEST_CODE
        ) {
            val intent = Intent(activity, CheckoutTaxActivity::class.java).apply {
                putExtra(REQUEST_CODE_EXTRA, requestCode)
            }

            activity.startActivityForResult(intent, requestCode)
        }
    }
}