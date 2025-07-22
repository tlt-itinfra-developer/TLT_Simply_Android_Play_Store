package tlt.th.co.toyotaleasing.modules.newsandpromotion.traffic

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_traffic.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.model.response.GetUserTwitterTimelineResponse

class TrafficFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TrafficViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_traffic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInstance()
        initPullToRefreshListener()
        initViewModel()

        viewModel.getData()
    }

    private fun initPullToRefreshListener() {
        swipe_refresh.setOnRefreshListener {
            swipe_refresh.isRefreshing = false
            viewModel.getData()
        }
    }

    private fun initInstance() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TrafficAdapter()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
//            toggleLoadingScreen(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            viewModel.getUserTimeLine(it!!)
        })

        viewModel.whenUserTimelineLoaded.observe(this, Observer {
            val adapter = recyclerView.adapter as TrafficAdapter
            val arrayList = ArrayList<GetUserTwitterTimelineResponse>(it)
            adapter.updateItems(arrayList)
        })
    }

    companion object {
        fun newInstance() = TrafficFragment()
    }


}
