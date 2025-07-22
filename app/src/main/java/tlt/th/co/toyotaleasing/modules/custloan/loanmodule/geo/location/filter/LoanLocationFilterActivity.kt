package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.geo.location.filter

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_loan_location_filter.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.util.PermissionUtils
import android.text.TextWatcher

class LoanLocationFilterActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoanLocationFilterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_loan_location_filter)

        initViewModel()
        initInstances()

        viewModel.getData()
        viewModel.showroom()
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

        viewModel.whenLoadShowroom.observe(this, Observer {
            val adapter  = ArrayAdapter(this,  android.R.layout.simple_list_item_1, it!!)
            edittext_company.setAdapter(adapter)
        })
    }

    private fun initInstances() {
//        edittext_company.onWatcherAfterTextChanged { anyFormForEnableSearch() }

        edittext_company.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                anyFormForEnableSearch()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

//        edittext_company.onText
        checkbox_office.setOnClickListener {
//            AnalyticsManager.mapSearchOffice()
            anyFormForEnableSearch()
        }
        checkbox_showroom.setOnClickListener {
//            AnalyticsManager.mapSearchShowroom()
            anyFormForEnableSearch()
        }
        checkbox_service.setOnClickListener {
//            AnalyticsManager.mapSearchServiceCenter()
            anyFormForEnableSearch()
        }
        checkbox_repair.setOnClickListener {
//            AnalyticsManager.mapSearchBodyPaintCenter()
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

    private fun supportForStaff(it: LoanLocationFilterViewModel.Model) {

    }

    private fun search(isNearly: Boolean = false) {
        viewModel.search(
                dealerName = edittext_company.text.toString().trim(),
                dealerCode = "",
                provinceIndex = spinner_province.getSelectedPositionWithoutHint(),
                amphurIndex = spinner_district.getSelectedPositionWithoutHint(),
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
//        AnalyticsManager.trackScreen(AnalyticsScreenName.MAP_DEALER_SEARCH)
    }

    companion object {
        const val REQUEST_CODE = 456

        fun startWithResult(context: Activity?) {
            val intent = Intent(context, LoanLocationFilterActivity::class.java)
            context?.startActivityForResult(intent, REQUEST_CODE)
        }
    }
}
