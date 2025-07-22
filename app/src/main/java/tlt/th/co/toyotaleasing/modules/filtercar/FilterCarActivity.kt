package tlt.th.co.toyotaleasing.modules.filtercar

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_filter_car.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity

class FilterCarActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FilterCarViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_car)

        initViewModel()
        initInstances()

        viewModel.getFilterCarList()
    }

    private fun initViewModel() {
        viewModel.whenGetFilterCarList.observe(this, Observer {
            val adapter = recycler_view.adapter as FilterCarAdapter
            adapter.addItems(it!!)
        })
    }

    private fun initInstances() {
        recycler_view.isNestedScrollingEnabled = false
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = FilterCarAdapter(ArrayList(), onFilterCarClickListener)

        toolbar.setOnRightMenuIconClickListener {
            AnalyticsManager.selectCarCloseClicked()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSTALLMENT_MY_CAR_SELECT_CAR)
    }

    private val onFilterCarClickListener = object : FilterCarAdapter.OnInteractionListener {
        override fun onClick(item: FilterCar) {
            AnalyticsManager.selectCarRegistrationClicked()
            viewModel.selectedCar(item.contractNumber)
            finish()
        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, FilterCarActivity::class.java)
            context.startActivity(intent)
        }
    }
}
