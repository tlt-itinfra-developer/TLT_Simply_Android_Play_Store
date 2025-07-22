package tlt.th.co.toyotaleasing.modules.insurance.other

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_other_insurance.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.*
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.modules.filtercar.FilterCarActivity
import tlt.th.co.toyotaleasing.modules.home.customer.MainCustomerActivity
import tlt.th.co.toyotaleasing.modules.insurance.quotation.QuotationActivity

class InsuranceOtherFragment : BaseFragment() {

    private lateinit var contractNoExtra: String

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(InsuranceOtherViewModel::class.java)
    }

    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_insurance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initInstances()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == QuotationActivity.REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            // Do Something
        }
    }

    private fun initViewModel() {


        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!, this)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                val adapter = recycler_view.adapter as InsuranceOtherAdapter
                adapter.updateItems(it)

            }
        })

    }


    private fun initInstances() {

        contractNoExtra = activity?.intent
                ?.getBundleExtra(MainCustomerActivity.DATA_POSITION_EXTRA)
                ?.getString(CONTRACT_NO_EXTRA) ?: ""

        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.insuranceMainMenuHome()
            onHambergerClickListener.onHambergerClick()
        }

        toolbar.setOnRightMenuTitleClickListener {
            AnalyticsManager.insuranceMainSelector()
            FilterCarActivity.open(context!!)
        }


        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = InsuranceOtherAdapter(arrayListOf(), onEventClickListener)
        }

    }

    private val onEventClickListener = object : InsuranceOtherAdapter.OnEventClickListener {
        override fun onClick(item: InsuranceOtherViewModel.Model) {

        }
    }

    companion object {
        const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"

        fun newInstance() = InsuranceOtherFragment()


    }
}
