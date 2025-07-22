package tlt.th.co.toyotaleasing.modules.qrcode.detail

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_qr_code.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.captureToExternalStorage
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.payment.checkout.adapter.PaymentListInformationAdapter


class QRActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(QRViewModel::class.java)
    }

    private val isCheckoutRequest by lazy {
        intent.getIntExtra(REQUEST_CODE_EXTRA, CHECKOUT_REQUEST_CODE) == CHECKOUT_REQUEST_CODE
    }

    private val transactionType = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code)

        initInstances()
        initViewModel()

    }

    private fun initInstances() {
//        changeScreenBrightness(255)

        if (isCheckoutRequest) {
            group_pay_detail.visible()
        } else {
            group_pay_detail.gone()
        }

        if (transactionType == TransactionType.TAX) {
            qr_code_footer_payment_message_tv.visible()
        }

        val resTitle = when (transactionType) {
            TransactionType.INSTALLMENT -> R.string.qr_code_payment_installment_title
            TransactionType.INSURANCE -> R.string.qr_code_payment_insurance_title
            TransactionType.TAX -> R.string.qr_code_payment_tax_title
            else -> R.string.qr_code_payment_other_title
        }

        qr_code_title_message_tv.text = getString(resTitle)

        qr_code_payment_list_rv.apply {
            this.layoutManager = LinearLayoutManager(this@QRActivity)
            this.adapter = PaymentListInformationAdapter()
        }

        qr_code_save_tv.setOnClickListener { captureScreenShot() }
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun setupDataIntoViews(data: QRViewModel.Model) {
        qr_code_owner_name_tv.text = data.fullName
        qr_code_owner_license_car_tv.text = data.carLicense
        qr_code_title_contract_tv.text = getString(R.string.my_car_txt_contract_number, data.contractNumber)

        qr_code_reference_no_1_tv.text = getString(R.string.qrcode_ref1_txt, data.referenceNo1)
        qr_code_reference_no_2_tv.text = getString(R.string.qrcode_ref2_txt, data.referenceNo2)
        qr_code_payment_total_tv.text = data.totalPrice

        barcode_txt.text = data.barcodeDisplay

        val adapter = qr_code_payment_list_rv.adapter as PaymentListInformationAdapter
        adapter.updateItems(data.fileList)

        renderQRCode(data.qrcode)
        renderBarcode(data.barcode)
    }

    private fun supportForStaff(it: QRViewModel.Model) {

    }

    private fun renderBarcode(text: String = "") {
        qr_barcode_iv.viewTreeObserver.addOnGlobalLayoutListener {
            val multiFormatWriter = MultiFormatWriter()

            try {
                val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODE_128, qr_barcode_iv.width, 250)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                qr_barcode_iv.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }

    private fun renderQRCode(text: String = "") {
        qr_qrcode_iv.viewTreeObserver.addOnGlobalLayoutListener {
            val multiFormatWriter = MultiFormatWriter()

            try {
                val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, qr_qrcode_iv.width, qr_qrcode_iv.height)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                qr_qrcode_iv.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }

    private fun captureScreenShot() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        qr_code_cardview.captureToExternalStorage(applicationContext)
                        Toast.makeText(this@QRActivity, "Saved Barcode Picture.", Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    companion object {
        const val REQUEST_CODE_EXTRA = "REQUEST_CODE_EXTRA"

        const val CHECKOUT_REQUEST_CODE = 982
        const val QR_ALLCAR_REQUEST_CODE = 983

        private const val TRANSACTION_TYPE_EXTRA = "TRANSACTION_TYPE_EXTRA"

        fun start(activity: Activity?, requestCode: Int, transactionType: TransactionType) {
            val intent = Intent(activity, QRActivity::class.java).apply {
                putExtra(REQUEST_CODE_EXTRA, requestCode)
                putExtra(TRANSACTION_TYPE_EXTRA, transactionType.name)
            }

            activity?.startActivityForResult(intent, requestCode)
        }

        fun start(fragment: Fragment?, requestCode: Int, transactionType: TransactionType) {
            val intent = Intent(fragment?.context, QRActivity::class.java).apply {
                putExtra(REQUEST_CODE_EXTRA, requestCode)
                putExtra(TRANSACTION_TYPE_EXTRA, transactionType.name)
            }

            fragment?.startActivityForResult(intent, requestCode)
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun changeScreenBrightness(brightnessValue: Int) {
        val settingCanWrite = Settings.System.canWrite(applicationContext)
        if (!settingCanWrite) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
        } else {
            Toast.makeText(this, "You have system write settings permission now.", Toast.LENGTH_SHORT).show()
        }
        // Change the screen brightness change mode to manual.
        Settings.System.putInt(applicationContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)
        // Apply the screen brightness value to the system, this will change the value in Settings ---> Display ---> Brightness level.
        // It will also change the screen brightness for the device.
        Settings.System.putInt(applicationContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightnessValue)

        /*
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.screenBrightness = screenBrightnessValue / 255f;
        window.setAttributes(layoutParams);
        */
    }

    enum class TransactionType {
        INSTALLMENT,
        TAX,
        INSURANCE,
        OTHER
    }
}