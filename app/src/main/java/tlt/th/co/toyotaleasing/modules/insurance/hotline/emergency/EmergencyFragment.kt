package tlt.th.co.toyotaleasing.modules.insurance.hotline.emergency

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_hotline_emergency.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.ifTrue

class EmergencyFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(EmergencyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotline_emergency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLayoutManager()
        initViewModel()
        viewModel.getData()
    }

    private fun initLayoutManager() {
        hotline_emergency_rv.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initViewModel() {
        viewModel.data.observe(this, Observer {
            it?.let {
                hotline_emergency_rv.adapter = EmergencyAdapter(it.emergencyList)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    private fun supportForStaff(it: EmergencyViewModel.Model) {

    }

    companion object {
        fun newInstance() = EmergencyFragment()
    }
}