package tlt.th.co.toyotaleasing.modules.payment.cart.installment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_cart.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.setText
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartAdapter
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.cart.customize.CartCustomizeActivity
import tlt.th.co.toyotaleasing.modules.payment.checkout.CheckoutInstallmentActivity

class CartInstallmentActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CartInstallmentViewModel::class.java)
    }

    private val isPayoff by lazy {
        intent.getBooleanExtra(IS_PAYOFF, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initViewModel()
        initInstances()
        viewModel.getData(isPayoff)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CartCustomizeActivity.CODE
                && resultCode == Activity.RESULT_OK) {
            viewModel.refreshListByLocalDB()
        }
    }

    private fun initViewModel() {
        viewModel.whenFailure.observe(this, Observer {
            showToast(it ?: "")
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
            it.isStaffApp.ifTrue { supportForStaff(it) }
        })

        viewModel.whenSubmitButtonStateChanged.observe(this, Observer {
            btn_summary_insurance_payment_total_paid.isEnabled = it!!
        })
    }

    private fun initInstances() {
        btn_add_summary.setOnClickListener {
            AnalyticsManager.paymentListAdjustPayment()
            viewModel.saveList()
            CartCustomizeActivity.startForResult(this , isPayoff)
        }

        btn_summary_insurance_payment_total_paid.setOnClickListener {
            AnalyticsManager.paymentListPayment()
            viewModel.submit()
            CheckoutInstallmentActivity.start(this)
        }
    }

    private fun setupDataIntoViews(it: CartViewModel.Model) {
        btn_summary_insurance_payment_total_paid.visible()
        text_summary_insurance_payment_date.text = it.currentDate
        text_summary_insurance_payment_car_license.text = it.carLicense
        text_summary_insurance_payment_contract_number.text = getString(R.string.my_car_txt_contract_number, it.contractNumber)
        text_summary_insurance_payment_total_paid.setText(getString(R.string.summary_edit_payment_total_amount), it.totalPaid, getString(R.string.currency, ""), R.color.terracotta)

        recycler_view.isNestedScrollingEnabled = false
        recycler_view.adapter = CartAdapter(it.cartList, onSummaryItemCheckListener , isPayoff )

        btn_add_summary.visible()

        title_detail_description_payment.visibility = View.VISIBLE
        divier.visibility = View.VISIBLE

    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSTALLMENT_PAYMENT_LIST)
    }

    private fun supportForStaff(it: CartViewModel.Model) {

    }

    private val onSummaryItemCheckListener = { index: Int, item: CartItem ->
        viewModel.updateSummaryItem(index, item)
        viewModel.refreshList()
    }

    companion object {
        const val IS_PAYOFF = "isPayoff"

        fun start(context: Context?) {
            val intent = Intent(context, CartInstallmentActivity::class.java)
            context?.startActivity(intent)
        }

        fun startByPayoff(context: Context ,  isPayoff: Boolean  ) {
            val intent = Intent(context, CartInstallmentActivity::class.java)
            intent.putExtra(IS_PAYOFF, isPayoff)
            context?.startActivity(intent)
        }
    }
}