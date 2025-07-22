package tlt.th.co.toyotaleasing.modules.payment.cart.insurance

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cart.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.setText
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartAdapter
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartViewModel
import tlt.th.co.toyotaleasing.modules.payment.checkout.CheckoutInsuranceActivity

class CartInsuranceActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CartInsuranceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenFailure.observe(this, Observer {
            showToast(it ?: "")
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenSubmitButtonStateChanged.observe(this, Observer {
            btn_summary_insurance_payment_total_paid.isEnabled = it!!
        })
    }

    private fun initInstances() {
        btn_summary_insurance_payment_total_paid.setOnClickListener {
            viewModel.submit()
            CheckoutInsuranceActivity.start(this)
        }
    }

    private fun setupDataIntoViews(it: CartViewModel.Model) {
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
        fun start(context: Context?) {
            val intent = Intent(context, CartInsuranceActivity::class.java)
            context?.startActivity(intent)
        }
    }
}