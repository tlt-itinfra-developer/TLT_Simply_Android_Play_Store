package tlt.th.co.toyotaleasing.modules.payment.cart.tax

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cart.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.setText
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartAdapter
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.checkout.CheckoutTaxActivity
import tlt.th.co.toyotaleasing.modules.tax.porlorbor.PorlorborTaxActivity
import tlt.th.co.toyotaleasing.modules.tax.torloraor.TorloraorTaxActivity

class CartTaxActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CartTaxViewModel::class.java)
    }

    private val isPorlorborBuyNowExtra by lazy {
        intent.getBooleanExtra(IS_PORLORBOR_BUY_NOW_EXTRA, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initViewModel()
        initInstances()

        viewModel.setIsPorlorborBuyNow(isPorlorborBuyNowExtra)
        viewModel.getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CheckoutTaxActivity.DEFAULT_REQUEST_CODE
                && resultCode == Activity.RESULT_CANCELED
                && viewModel.isShowPorlorborAttachScreen()
                && !viewModel.isShowTorloraorAttachScreen()) {
            PorlorborTaxActivity.startWithResult(this, true)
            return
        }

        if (requestCode == CheckoutTaxActivity.DEFAULT_REQUEST_CODE
                && resultCode == Activity.RESULT_CANCELED
                && viewModel.isShowTorloraorAttachScreen()) {
            TorloraorTaxActivity.startWithResult(this, true)
            return
        }

        if (requestCode == PorlorborTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && viewModel.isShowTorloraorAttachScreen()) {
            TorloraorTaxActivity.startWithResult(this, true)
            return
        }

        if (requestCode == TorloraorTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_CANCELED
                && viewModel.isShowPorlorborAttachScreen()) {
            PorlorborTaxActivity.startWithResult(this, true)
            return
        }

        if (requestCode == PorlorborTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK
                && !viewModel.isShowTorloraorAttachScreen()) {
            CheckoutTaxActivity.startWithResult(this, CheckoutTaxActivity.DEFAULT_REQUEST_CODE)
            return
        }

        if (requestCode == TorloraorTaxActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            CheckoutTaxActivity.startWithResult(this, CheckoutTaxActivity.DEFAULT_REQUEST_CODE)
            return
        }
    }

    private fun initViewModel() {
        viewModel.whenFailure.observe(this, Observer {
            showToast(it ?: "")
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenSubmitButtonStateChanged.observe(this, Observer {
            setupButton()
            btn_summary_insurance_payment_total_paid.isEnabled = it!!
            btn_attach_document_process.isEnabled = it
        })
    }

    private fun initInstances() {
        text_summary_insurance_payment_title.text = ContextManager.getInstance().getStringByRes(R.string.summary_tax_payment_header)
        btn_summary_insurance_payment_total_paid.setOnClickListener {
            AnalyticsManager.taxPaymentListPayment()
            viewModel.submit()

            if (viewModel.isShowPorlorborAttachScreen()) {
                PorlorborTaxActivity.startWithResult(this, true)
                return@setOnClickListener
            }

            if (!viewModel.isShowPorlorborAttachScreen()
                    && viewModel.isShowTorloraorAttachScreen()) {
                TorloraorTaxActivity.startWithResult(this, true)
                return@setOnClickListener
            }

            CheckoutTaxActivity.startWithResult(this, CheckoutTaxActivity.DEFAULT_REQUEST_CODE)
        }

        btn_attach_document_process.setOnClickListener {
            AnalyticsManager.taxPaymentListUploadDoc()
            viewModel.submit()

            if (viewModel.isShowPorlorborAttachScreen()) {
                PorlorborTaxActivity.startWithResult(this, true)
                return@setOnClickListener
            }

            if (!viewModel.isShowPorlorborAttachScreen()
                    && viewModel.isShowTorloraorAttachScreen()) {
                TorloraorTaxActivity.startWithResult(this, true)
                return@setOnClickListener
            }

            CheckoutTaxActivity.startWithResult(this, CheckoutTaxActivity.DEFAULT_REQUEST_CODE)
        }
    }

    private fun setupButton() {

        if (viewModel.isShowPorlorborAttachScreen()
                || viewModel.isShowTorloraorAttachScreen()) {
            btn_summary_insurance_payment_total_paid.gone()
            btn_attach_document_process.visible()
        } else {
            btn_summary_insurance_payment_total_paid.visible()
            btn_attach_document_process.gone()
        }
    }

    private fun setupDataIntoViews(it: CartViewModel.Model) {
        setupButton()
        text_summary_insurance_payment_date.text = it.currentDate
        text_summary_insurance_payment_car_license.text = it.carLicense
        text_summary_insurance_payment_contract_number.text = getString(R.string.my_car_txt_contract_number, it.contractNumber)
        text_summary_insurance_payment_total_paid.setText(getString(R.string.receipt_total_paid), it.totalPaid, getString(R.string.currency, ""), R.color.terracotta)

        recycler_view.isNestedScrollingEnabled = false
        recycler_view.adapter = CartAdapter(it.cartList, onSummaryItemCheckListener)

        btn_add_summary.gone()
    }

    private val onSummaryItemCheckListener = { index: Int, item: CartItem ->
        viewModel.updateSummaryItem(index, item)
        viewModel.refreshList()
    }

    companion object {
        private const val IS_PORLORBOR_BUY_NOW_EXTRA = "IS_PORLORBOR_BUY_NOW_EXTRA"

        fun start(context: Context?, isPorlorborBuyNow: Boolean = false) {
            val intent = Intent(context, CartTaxActivity::class.java).apply {
                putExtra(IS_PORLORBOR_BUY_NOW_EXTRA, isPorlorborBuyNow)
            }

            context?.startActivity(intent)
        }
    }
}