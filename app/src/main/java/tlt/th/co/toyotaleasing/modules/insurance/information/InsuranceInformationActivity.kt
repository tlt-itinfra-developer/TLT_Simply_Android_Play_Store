package tlt.th.co.toyotaleasing.modules.insurance.information

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_insurance_information.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.insurance.hotline.HotLineActivity
import tlt.th.co.toyotaleasing.modules.insurance.recommend.InsuranceRecommendActivity

class InsuranceInformationActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InsuranceInformationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurance_information)
        initViewModel()
        initInstances()

        viewModel.getData()
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
                InsuranceInformationViewModel.Status.NORMAL -> normalStatus()
                else -> noDataStatus()
            }
        })
    }

    private fun initInstances() {
        insurance_information_history_rv.apply {
            layoutManager = LinearLayoutManager(this@InsuranceInformationActivity)
        }

        insurance_information_recommend_cl.setOnClickListener {
            AnalyticsManager.insuranceInfoRecommended()
            InsuranceRecommendActivity.start(this)
        }

        insurance_information_faqs_cl.setOnClickListener {
            AnalyticsManager.tibContactUs()
            HotLineActivity.start(this, HotLineActivity.CONTACT)
        }

        insurance_information_expand_tv.setOnClickListener {
            val adapter = insurance_information_history_rv.adapter as InsuranceInformationAdapter

            if (adapter.isExpand) {
                insurance_information_expand_tv.text = getString(R.string.insurance_information_hide_message)
                val arrowUpImageDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_up)
                insurance_information_expand_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUpImageDrawable, null)
            } else {
                insurance_information_expand_tv.text = getString(R.string.insurance_information_all_message)
                val arrowDownImageDrawable = ContextCompat.getDrawable(this, R.drawable.ic_arrow_down)
                insurance_information_expand_tv.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDownImageDrawable, null)
            }

            adapter.expandInsruanceHistory()
        }
    }

    private fun initHistoryAdapter(data: List<InsuranceHistoryData>) {
        insurance_information_history_rv.adapter = InsuranceInformationAdapter(data)
        insurance_information_history_rv.isNestedScrollingEnabled = false
    }

    private fun setupDataIntoViews(it: InsuranceInformationViewModel.Model?) {
        it?.let {
            insurance_information_car_view.initialInformationData(it.carInformationData)
            insurance_view.initialInformationData(it.insuranceInfomationData)
            insurance_information_calendar_tv.text = getString(R.string.insurance_expire_day, it.expireDays)
            initHistoryAdapter(it.historyData)
        }
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.TIB_INSURANCE_INFOR)
    }

    private fun supportForStaff(it: InsuranceInformationViewModel.Model) {

    }

    private fun normalStatus() {
        insurance_information_history_nodata_card_view.gone()
        insurance_information_history_card_view.visible()
    }

    private fun noDataStatus() {
        insurance_information_history_card_view.gone()
        insurance_information_history_nodata_card_view.visible()
    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, InsuranceInformationActivity::class.java)
            context?.startActivity(intent)
        }
    }
}