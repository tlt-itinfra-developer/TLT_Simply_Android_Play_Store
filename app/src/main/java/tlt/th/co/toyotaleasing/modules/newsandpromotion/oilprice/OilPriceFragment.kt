package tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_oil_price.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment

class OilPriceFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(OilPriceViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_oil_price, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstance()

//        viewModel.getData()
        try{
            viewModel.requestPTTSoapData()
        }catch (e : Exception) {
            Log.e("pttp oil price",e.message.toString() )
        }

    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            //            toggleLoadingScreen(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it.let {
                setupDataIntoViews(it)
            }
        })
    }

    private fun setupDataIntoViews(it: OilPriceViewModel.Model?) {
        current_date.text = it?.date

        val oilAdapter = recycler_view.adapter as OilPriceAdapter
        val gasAdapter = recycler_view_gas.adapter as OilPriceAdapter

        oilAdapter.updateItem(it?.oilList ?: listOf())
        gasAdapter.updateItem(it?.gasList ?: listOf())
    }

    private fun initInstance() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OilPriceAdapter()
            ViewCompat.setNestedScrollingEnabled(this, false)
        }

        recycler_view_gas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = OilPriceAdapter()
            ViewCompat.setNestedScrollingEnabled(this, false)
        }

        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
//            viewModel.getData()
            viewModel.requestPTTSoapData()
        }
    }

    companion object {
        fun newInstance() = OilPriceFragment()
    }
}
