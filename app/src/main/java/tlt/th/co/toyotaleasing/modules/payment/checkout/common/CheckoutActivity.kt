package tlt.th.co.toyotaleasing.modules.payment.checkout.common

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.modules.payment.checkout.adapter.PaymentFileDocumentAdapter
import tlt.th.co.toyotaleasing.modules.payment.checkout.adapter.PaymentListInformationAdapter
import tlt.th.co.toyotaleasing.modules.payment.checkout.adapter.PaymentMethodAdapter
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptDelayActivity

abstract class CheckoutActivity : BaseActivity() {

    protected val viewModel by lazy {
        ViewModelProviders.of(this).get(CheckoutViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initViewModel()
        initInstances()

        onViewReady()
        initPaymentTitle()
    }

    private fun initPaymentTitle() {
        txt_payment_title.text = onPaymentTitleChange()
    }

    private fun initViewModel() {
        viewModel.whenPushReceiptPage.observe(this, Observer {
            ReceiptDelayActivity.start(this, true, it!!)
        })

        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer { it ->
            it?.let {
                txt_payment_date.text = it.date
                txt_payment_license_car_no.text = it.licenseCarNo
                txt_payment_contract_no.text = getString(R.string.my_car_txt_contract_number, it.contractNumber)
                txt_payment_total_tv.setText(getString(R.string.payment_summary),
                        it.totalPrice,
                        getString(R.string.account_closing_baht), R.color.terracotta)

                initPaymentAdapter(it.fileList, it.listInformationList, it.payment)

                if(it.payment.size > 0 ) {
                    txt_payment_method_title.visible()
                    rv_payment_method_choice.visible()
                }else{
                    txt_payment_method_title.gone()
                    rv_payment_method_choice.gone()
                }

                if(it.wallet.size > 0 ) {
                    txt_wallet_method_title.visible()
                    rv_wallet_method_choice.visible()
                    initWalletAdapter( it.wallet)
                }else{
                    txt_wallet_method_title.gone()
                    rv_wallet_method_choice.gone()
                }

                if(it.mobilebanking.size > 0 ) {
                    txt_mbank_method_title.visible()
                    rv_mbank_method_choice.visible()
                    initMobileBankingPaymentAdapter( it.mobilebanking)
                }else{
                    txt_mbank_method_title.gone()
                    rv_mbank_method_choice.gone()
                }

                if(it.qrChannel.size > 0 ) {
                    txt_qr_method_title.visible()
                    rv_qr_method_choice.visible()
                    initQRPaymentAdapter(it.qrChannel)
                }else{
                    txt_qr_method_title.gone()
                    rv_qr_method_choice.gone()
                }

                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
//
//        viewModel.whenSubmitButtonStateChanged.observe(this, Observer {
//            iv_payment_cash.isEnabled = it!!
//        })

        viewModel.whenBarcodeIsOpening.observe(this, Observer {
            AnalyticsManager.paymentChannelQr()
            onBarcodeClick()
        })

        viewModel.whenIBankIsOpening.observe(this, Observer {
            AnalyticsManager.paymentChannelBank(it!!)
            onPaymentClick()
        })
    }

    private fun supportForStaff(it: CheckoutViewModel.Model) {

    }

    private fun initInstances() {
        initLayoutManager()

//        iv_payment_cash.setOnClickListener {
//
//        }
    }

    private fun initLayoutManager() {
        rv_payment_file_document_information.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
        }

        rv_payment_information.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
            isNestedScrollingEnabled = false
        }

        rv_payment_method_choice.apply {
            layoutManager = GridLayoutManager(this@CheckoutActivity, 3)
            isNestedScrollingEnabled = false
        }

        rv_wallet_method_choice.apply {
            layoutManager = GridLayoutManager(this@CheckoutActivity, 3)
            isNestedScrollingEnabled = false
        }


        rv_mbank_method_choice.apply {
            layoutManager = GridLayoutManager(this@CheckoutActivity, 3)
            isNestedScrollingEnabled = false
        }

        rv_qr_method_choice.apply {
            layoutManager = GridLayoutManager(this@CheckoutActivity, 3)
            isNestedScrollingEnabled = false
        }
    }

    private fun initPaymentAdapter(fileList: List<CheckoutViewModel.Model.FileDocumentModel> = listOf(),
                                   informationList: List<CheckoutViewModel.Model.ListInformationModel> = listOf(),
                                   checkout: List<CheckoutViewModel.Model.PaymentMethodModel> = listOf()) {
        rv_payment_information.adapter = PaymentListInformationAdapter(informationList.toMutableList())
        rv_payment_method_choice.adapter = PaymentMethodAdapter(checkout.toMutableList(), paymentMethodListener)

        if (fileList.isNotEmpty()) {
            val images = fileList.map { it.imageUrl }
            rv_payment_file_document_information.adapter = PaymentFileDocumentAdapter(images, onImageClickListener)
        }
    }

    private fun initWalletAdapter(checkout: List<CheckoutViewModel.Model.PaymentMethodModel> = listOf()) {
        rv_wallet_method_choice.adapter = PaymentMethodAdapter(checkout.toMutableList(), paymentMethodListener)

    }

    private fun initMobileBankingPaymentAdapter(checkout: List<CheckoutViewModel.Model.PaymentMethodModel> = listOf()) {
        rv_mbank_method_choice.adapter = PaymentMethodAdapter(checkout.toMutableList(), paymentMethodListener)

    }

    private fun initQRPaymentAdapter(checkout: List<CheckoutViewModel.Model.PaymentMethodModel> = listOf()) {
        rv_qr_method_choice.adapter = PaymentMethodAdapter(checkout.toMutableList(), paymentMethodListener)

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.PAYMENT_CHANNEL)
    }

    private val paymentMethodListener = { position: Int, checkoutMethodModel: CheckoutViewModel.Model.PaymentMethodModel ->
           viewModel.selectPaymentMethod(position, checkoutMethodModel)
           val type = getCheckoutType()
           val channelPayment  = checkoutMethodModel.callBankDesc.toString()
           val descType  = checkoutMethodModel.channel.toString()
           viewModel.submit(type , channelPayment , descType)
    }

    private val onImageClickListener = { imageBase64: String, position: Int ->

    }

    abstract fun onViewReady()
    abstract fun onPaymentTitleChange(): String
    abstract fun getCheckoutType(): String
    abstract fun onPaymentClick()
    abstract fun onBarcodeClick()
    abstract fun setImageList()

    companion object {
        const val INSTALLMENT_TYPE = "CONTRACT"
        const val TAX_TYPE = "TAX"
        const val INSURANCE_TYPE = "INSURANCE"

        fun start(context: Context) {
            val intent = Intent(context, CheckoutActivity::class.java)
            context.startActivity(intent)
        }
    }
}