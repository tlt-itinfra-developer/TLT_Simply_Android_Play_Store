package tlt.th.co.toyotaleasing.modules.payment.receipt.common

import android.Manifest
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_receipt.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment
import tlt.th.co.toyotaleasing.common.extension.*

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import java.lang.Exception

abstract class ReceiptActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ReceiptViewModel::class.java)
    }

    var isDelay: Boolean = false
    var receiptData: CheckStatusFromBankResponse = CheckStatusFromBankResponse()

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt)

        initialData()
        initViewModel()
        initInstances()

        viewModel.getData(isDelay = isDelay,
                receiptData = receiptData)

    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            isDelay.ifFalse {
                //DatabaseManager.getInstance().deleteSequenceIdLast()
            }
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun initInstances() {
        txt_insurance_contact.movementMethod = LinkMovementMethod.getInstance()
        txt_insurance_contact.setOnClickListener {
            AnalyticsManager.receiptCall()
        }

        title_save_slip.setOnClickListener {
            AnalyticsManager.receiptSave()
            captureScreenShot(isShowDialog = true)
        }

        btn_backhome.setOnClickListener {
            AnalyticsManager.receiptBackMainPage()
            onBackToHomeClick()
        }

        title_send_email.setOnClickListener {
            AnalyticsManager.receiptSendEmail()
            captureScreenShot(isShowDialog = false)
        }
    }

    private fun sendEmailWithAttachFile() {
        val emailIntent = Intent(android.content.Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, UserManager.getInstance().getProfile().email)
        emailIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        emailIntent.type = "message/rfc822"
        startActivity(Intent.createChooser(emailIntent, "Send text file"))
    }

    private fun setupDataIntoViews(it: ReceiptViewModel.Model) {
        txt_current_date.text = it.currentDate
        txt_fullname.text = it.fullname
        txt_car_license.text = it.carLicense
        txt_contract_number.text = it.contractNumber
        txt_payment_method.text = it.paymentMethod
        txt_total_paid.text = getString(R.string.currency, it.totalPaid)

        isDelay.ifTrue {
            when (it.type) {
                ReceiptViewModel.Type.INSTALLMENT -> {
                    txt_payment_type.text = getString(R.string.receipt_payment_type_installment)
                    description.visible()
                    description.text = getString(R.string.receipt_description_insatllment_paid_success)
                }
                ReceiptViewModel.Type.INSURANCE -> {
                    txt_payment_type.text = getString(R.string.receipt_payment_type_insurance)
                    description.visible()
                    description.text = getString(R.string.receipt_description_insurance_paid_success)
                }
                ReceiptViewModel.Type.TAX -> {
                    txt_payment_type.text = getString(R.string.receipt_payment_type_tax)
                    description.gone()
                }
                ReceiptViewModel.Type.OTHER -> {
                    txt_payment_type.text = "-"
                    description.gone()
                }
            }
        }

        captureScreenShot(isShowDialog = true)

        when (it.status) {
            ReceiptViewModel.Status.PAID_FAIL -> paidFail()
            else -> onPaymentSuccess()
        }

    }

    private fun supportForStaff(it: ReceiptViewModel.Model) {

    }

    private fun captureScreenShot(isShowDialog: Boolean) {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        if (isShowDialog) {
                            try {
                                imageUri = layout_receipt.captureToExternalStorage(applicationContext)
                                NormalDialogFragment.show(
                                        fragmentManager = supportFragmentManager,
                                        description = getString(R.string.receipt_save_image_success),
                                        confirmButtonMessage = getString(R.string.dialog_button_ok)
                                )
                            }catch (e : Exception){
                                imageUri = layout_receipt.captureToExternalStorageAbsolute(applicationContext = applicationContext)
                            }
                        } else {
                            imageUri = layout_receipt.captureToExternalStorageAbsolute(applicationContext = applicationContext)
                            sendEmailWithAttachFile()
                        }
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {

                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.PAYMENT_SUCCESS)
    }

    private fun paidFail() {
        layout_paid_success.gone()
    }

    abstract fun onPaymentSuccess()
    abstract fun onBackToHomeClick()
    abstract fun initialData()

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, ReceiptActivity::class.java)
            context?.startActivity(intent)
        }
    }
}