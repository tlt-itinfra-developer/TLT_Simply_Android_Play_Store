package tlt.th.co.toyotaleasing.modules.insurance.status

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_insurance_status.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible

class InsuranceStatusActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InsuranceStatusViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_status)

        initInstance()
        initViewModel()

        viewModel.getData()
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.TIB_INSURACE_POLICY)
    }

    private fun initInstance() {
        insurance_status_web_tv.setOnClickListener {
            AnalyticsManager.insurancePolicyStatusEmsLink()
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(getString(R.string.status_insurance_web_url))
            startActivity(browserIntent)
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
    }

    private fun setupDataIntoViews(it: InsuranceStatusViewModel.Model?) {
        it?.let {
            insurance_status_car_view.initialInformationData(it.carInformationData)
            insurance_status_information_view.initialInformationData(it.insuranceInfomationData)
            insurance_status_date_tv.text = it.sentDate

            if (it.trackingNo.isNullOrEmpty()) {
                group_normal.gone()
                no_data_tv.visible()
            } else {
                group_normal.visible()
                no_data_tv.gone()
                insurance_status_contract_tv.text = getString(R.string.insurance_status_tracking_no, it.trackingNo)
            }
        }
    }

    private fun supportForStaff(it: InsuranceStatusViewModel.Model) {

    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, InsuranceStatusActivity::class.java)
            context?.startActivity(intent)
        }
    }
}