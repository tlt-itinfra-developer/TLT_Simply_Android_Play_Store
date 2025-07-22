package tlt.th.co.toyotaleasing.modules.insurance.paymentdetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_insurance_payment_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.invisible
import tlt.th.co.toyotaleasing.common.extension.setText
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.payment.cart.insurance.CartInsuranceActivity
import tlt.th.co.toyotaleasing.view.insurance.CarInformationWidget
import tlt.th.co.toyotaleasing.view.insurance.InsurancePaymentWidget

class PaymentDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PaymentDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_payment_detail)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            setupDataIntoViews(it!!)
        })

        viewModel.whenDisplayUiByStatus.observe(this, Observer {
            when (it!!) {
                PaymentDetailViewModel.Status.PAYABLE -> payable()
                else -> unpayable()
            }
        })
    }

    private fun initInstances() {
        insurance_error_tv.movementMethod = LinkMovementMethod.getInstance()

        insurance_error_tv.setOnClickListener {
            AnalyticsManager.insurancePaymentDetailCall()
        }

        insurance_payment_btn.setOnClickListener {
            AnalyticsManager.insurancePaymentDetailPayment()
            CartInsuranceActivity.start(this)
        }
    }

    private fun setupDataIntoViews(it: PaymentDetailViewModel.Model) {
        insurance_status_car_view.initialInformationData(CarInformationWidget.CarInformationData(
                it.carLicense,
                it.fullname,
                getString(R.string.my_car_txt_contract_number, it.contractNumber),
                it.currentDate
        ))

        insurance_status_information_view.initialInformationData(InsurancePaymentWidget.InsurancePaymentInfomationData(
                it.insuranceCompany,
                it.insuranceProtection,
                getString(R.string.currency, it.insuranceBudget),
                it.conditionRepair,
                it.endOfProtectionDate
        ))

        insurance_payment_budget.setText(getString(R.string.insurance_payment_budget_pre),
                it.insurancePremium, getString(R.string.insurance_payment_budget_baht), R.color.terracotta)
    }

    private fun payable() {
        insurance_layout_pay.visible()
        insurance_layout_not_pay.invisible()
    }

    private fun unpayable() {
        insurance_layout_pay.gone()
        insurance_layout_not_pay.visible()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSURANCE_PAYMENT_DETAIL)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, PaymentDetailActivity::class.java)
            context?.startActivity(intent)
        }
    }
}