package tlt.th.co.toyotaleasing.modules.location.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_location_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.CallCenterDialogFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class LocationDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        initViewModel()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun setupDataIntoViews(item: LocationDetailViewModel.Model?) {
        txt_company_name.text = item?.company
        txt_branch_name.text = item?.branch
        txt_distance.text = getString(R.string.location_distance, item?.distance)
        txt_type.text = item?.officeType ?: ""
        txt_address.text = item?.address ?: ""
        txt_tel.text = item?.tel ?: ""
        txt_time.text = item?.time ?: ""
        txt_website.text = item?.website ?: ""

        location_tel.setOnClickListener {
            CallCenterDialogFragment.show(fragmentManager = supportFragmentManager,
                    displayPhoneNumber = item?.telForCall ?: "",
                    phoneNumber = item?.tel ?: "",
                    openBy = CallCenterDialogFragment.LOCATION_DETAIL)
        }

        location_direction.setOnClickListener {
            AnalyticsManager.mapDealerDetailRoute()
            ExternalAppUtils.openGoogleDirection(this, item!!.lat.toDouble(), item.lng.toDouble())
        }
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.MAP_DEALER_DETAIL)
    }

    private fun supportForStaff(it: LocationDetailViewModel.Model) {

    }

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, LocationDetailActivity::class.java)
            context?.startActivity(intent)
        }
    }
}
