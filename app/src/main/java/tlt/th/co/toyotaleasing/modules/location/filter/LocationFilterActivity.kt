package tlt.th.co.toyotaleasing.modules.location.filter

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_location_filter.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.onWatcherAfterTextChanged
import tlt.th.co.toyotaleasing.util.PermissionUtils

class LocationFilterActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LocationFilterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_filter)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenProvinceChanged.observe(this, Observer {
            spinner_province.setItems(it ?: listOf())
        })

        viewModel.whenAmphurChanged.observe(this, Observer {
            spinner_district.setItems(it ?: listOf())
        })
    }

    private fun initInstances() {
        edittext_company.onWatcherAfterTextChanged { anyFormForEnableSearch() }

        checkbox_office.setOnClickListener {
            AnalyticsManager.mapSearchOffice()
            anyFormForEnableSearch()
        }
        checkbox_showroom.setOnClickListener {
            AnalyticsManager.mapSearchShowroom()
            anyFormForEnableSearch()
        }
        checkbox_service.setOnClickListener {
            AnalyticsManager.mapSearchServiceCenter()
            anyFormForEnableSearch()
        }
        checkbox_repair.setOnClickListener {
            AnalyticsManager.mapSearchBodyPaintCenter()
            anyFormForEnableSearch()
        }

        spinner_province.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val realPosition = position - 1
                viewModel.amphurChanged(realPosition)
                anyFormForEnableSearch()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        btn_filter_nearly.setOnClickListener {
            AnalyticsManager.mapSearchNearby()
            search(isNearly = true)
        }

        btn_filter.setOnClickListener {
            AnalyticsManager.mapSearchSearch()
            search()
        }

        if (PermissionUtils.isGrantedLocation()) {
            btn_filter_nearly.isEnabled = true
        }
    }

    private fun supportForStaff(it: LocationFilterViewModel.Model) {

    }

    private fun search(isNearly: Boolean = false) {
        viewModel.search(
                dealerName = edittext_company.text.toString().trim(),
                provinceIndex = spinner_province.getSelectedPositionWithoutHint(),
                amphurIndex = spinner_district.getSelectedPositionWithoutHint(),
                isOfficeChecked = checkbox_office.isChecked,
                isShowRoomChecked = checkbox_showroom.isChecked,
                isServiceCenterChecked = checkbox_service.isChecked,
                isRepairServiceChecked = checkbox_repair.isChecked,
                isNearly = isNearly
        )

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun anyFormForEnableSearch() {
        if (edittext_company.text.toString().trim().isNotEmpty()
                || !spinner_province.isSelectHint()
                || !spinner_district.isSelectHint()) {
            btn_filter.isEnabled = true
            return
        }

        btn_filter.isEnabled = false
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.MAP_DEALER_SEARCH)
    }

    companion object {
        const val REQUEST_CODE = 456

        fun startWithResult(fragment: Fragment) {
            val intent = Intent(fragment.context, LocationFilterActivity::class.java)
            fragment.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
