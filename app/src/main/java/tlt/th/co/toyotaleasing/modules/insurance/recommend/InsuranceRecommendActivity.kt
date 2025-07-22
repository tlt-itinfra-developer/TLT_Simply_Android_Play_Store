package tlt.th.co.toyotaleasing.modules.insurance.recommend

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_insurance_recommend.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.modules.insurance.quotation.QuotationActivity
import tlt.th.co.toyotaleasing.modules.payment.cart.insurance.CartInsuranceActivity

class InsuranceRecommendActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InsuranceRecommendViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_recommend)
        initViewModel()
        initInstances()

        viewModel.getData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == QuotationActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            // Do something
        }
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenDisplayUiByStatus.observe(this, Observer {
            when (it) {
                InsuranceRecommendViewModel.Status.FOUND_RECOMMEND -> foundRecommendStatus()
                else -> notFoundRecommendStatus()
            }
        })
    }

    private fun initInstances() {
        not_found_recommend_no_title_tv.movementMethod = LinkMovementMethod.getInstance()

        btn_insurance_pay.setOnClickListener {
            AnalyticsManager.insuranceRecommendedPayment()
            CartInsuranceActivity.start(this)
        }

        recommend_promotion_card.setOnClickListener {
            AnalyticsManager.insuranceRecommendedQuotationForm()
            QuotationActivity.startWithResult(this)
        }
    }

    private fun setupDataIntoViews(it: InsuranceRecommendViewModel.Model?) {
        it?.let {
            recommend_car_view.initialInformationData(it.carInformationData)
            recommend_information_view.initialInformationData(it.insuranceInfomationData)

            it.historyData?.let {
                recommend_iv.loadImageByUrl(it.insuranceImgUrl)
                recommend_no_field_tv.text = it.insuranceNumber
                recommend_company_field_tv.text = it.insuranceCompany
                recommend_protection_type_field_tv.text = it.insuranceType
                recommend_insurance_value_field_tv.text = it.insuranceValue
                recommend_condition_field_tv.text = it.insuranceCondition
                recommend_end_of_protection_field_tv.text = it.insuranceEndOfDate
            }

            if (it.insuranceData.flag4M!!.toLowerCase() == "n") {
                notFoundRecommendStatus()
            } else {
                foundRecommendStatus()
            }

            txt_insurance_pay.setText(getString(R.string.insurance_payment_budget_pre),
                    it.totalPrice, getString(R.string.insurance_payment_budget_baht), R.color.terracotta)
        }
    }

    private fun supportForStaff(it: InsuranceRecommendViewModel.Model) {

    }

    private fun foundRecommendStatus() {
        group_found_recommend.visible()
        group_not_found_recommend.gone()
    }

    private fun notFoundRecommendStatus() {
        group_found_recommend.gone()
        group_not_found_recommend.visible()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSURANCE_RECOMMENDED)
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, InsuranceRecommendActivity::class.java)
            context?.startActivity(intent)
        }
    }
}