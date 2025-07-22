package tlt.th.co.toyotaleasing.modules.payment.cart.customize

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cart_customize.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.modules.payment.cart.common.CartItem
import tlt.th.co.toyotaleasing.modules.payment.receipt.ReceiptDelayActivity
import tlt.th.co.toyotaleasing.view.selectionmodal.SelectionBottomSheet
import tlt.th.co.toyotaleasing.view.selectionmodal.SelectionBottomSheetAdapter

class CartCustomizeActivity : BaseActivity(), SelectionBottomSheet.Listener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CartCustomizeViewModel::class.java)
    }

    private val isPayoff by lazy {
        intent.getBooleanExtra(IS_PAYOFF, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_customize)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
            it.isStaffApp.ifTrue { supportForStaff(it) }
        })

        viewModel.whenSummaryListUpdate.observe(this, Observer { data ->
            val adapter = recycler_view.adapter as CartCustomizeAdapter
            adapter.updateItems(data!!)
        })
    }

    private fun initInstances() {
        recycler_view.apply {
            isNestedScrollingEnabled = false
            adapter = CartCustomizeAdapter(
                    mutableListOf(),
                    onSummaryItemCheckListener,
                    onRemoveSummaryItemListener,
                    onOptionItemClickListener ,
                    isPayoff
            )
        }

        btn_add_summary.setOnClickListener {
            AnalyticsManager.adjustPaymentAddOther()
            if (!viewModel.isAdd()) {
                Toast.makeText(this@CartCustomizeActivity, "Can't add option", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val adapter = recycler_view.adapter as CartCustomizeAdapter
            viewModel.addSummaryItem(adapter.getItems().toMutableList())
        }

        btn_confirm.setOnClickListener {
            AnalyticsManager.adjustPaymentConfirm()
//            checkEmptyValues()
            val adapter = recycler_view.adapter as CartCustomizeAdapter
            if (checkEmptyValues(adapter.getItems().toList())) {
                if (checkZeroValues(adapter.getItems().toList())) {
                    viewModel.submit(adapter.getItems().toList())
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }

        }
    }

    private fun checkZeroValues(list: List<CartItem>): Boolean {
        return list.none {
            it._price.toDouble() == "0.0".toDouble()
        }
    }

    private fun checkEmptyValues(list: List<CartItem>): Boolean {
//        val list = (recycler_view.adapter as CartCustomizeAdapter).getItems().toList()
//        var index : Int  = 0
//        list.forEach {
//            if (it._price == ""){
//                (recycler_view.adapter as CartCustomizeAdapter).getItems().toList().get(index)._price = "0.00"
//            }
//            index += 1
//        }
        return list.none {
            it._price ==  ""
        }
    }

    private fun setupDataIntoViews(it: CartCustomizeViewModel.Model) {
        text_summary_insurance_payment_date.text = it.currentDate
        text_summary_insurance_payment_car_license.text = it.carLicense
        text_summary_insurance_payment_contract_number.text = getString(R.string.my_car_txt_contract_number, it.contractNumber)
    }

    private fun supportForStaff(it: CartCustomizeViewModel.Model) {

    }

    private val onSummaryItemCheckListener = { index: Int, item: CartItem ->
        viewModel.updateSummaryItem(index, item)
    }

    private val onRemoveSummaryItemListener = { index: Int, item: CartItem ->
        AnalyticsManager.adjustPaymentDelete()
        viewModel.removeSummaryItem(index)
    }

    private val onOptionItemClickListener = { index: Int, item: CartItem ->
        viewModel.clickingPosition = index

        val list = viewModel.getTitleOptionsList()
        val disableSelected = viewModel.getIndexsOfDisableOptions()

        SelectionBottomSheet.show(
                fragmentManager = supportFragmentManager,
                list = ArrayList(list),
                disableSelected = ArrayList(disableSelected)
        )
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSTALLMENT_ADJUST_PAYMENT)
    }

    override fun onItemSelectionClick(index: Int, item: SelectionBottomSheetAdapter.Model) {
        viewModel.updateSummaryItemByOptionItem(index)
    }

    companion object {
        const val CODE = 49
        const val IS_PAYOFF = "isPayoff"

        fun startForResult(activity: Activity ,  isPayoff: Boolean) {
            val intent = Intent(activity, CartCustomizeActivity::class.java)
            intent.putExtra(IS_PAYOFF, isPayoff)
            activity.startActivityForResult(intent, CODE)
        }


    }
}