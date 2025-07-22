package tlt.th.co.toyotaleasing.modules.contract.refinance

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_refinance.*
import kotlinx.android.synthetic.main.layout_refinance_active.*
import kotlinx.android.synthetic.main.layout_refinance_active.view.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager

class RefinanceFragment : BaseFragment() {

    private val SCROLL_POSITION_STATE = "scroll_position"

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(RefinanceViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_refinance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getDataRefinance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            nestedscrollview.scrollY = savedInstanceState.getInt(SCROLL_POSITION_STATE, 0)
        }
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenShowStatus.observe(this, Observer {
            when (it) {
                RefinanceViewModel.Status.MATCH_CONDITION -> matchCondition()
                RefinanceViewModel.Status.NOT_MATCH_CONDITION -> notMatchCondition()
            }
        })
    }

    private fun setupDataIntoViews(it: RefinanceViewModel.Model) {
        txt_car_license.text = it.carLicense
        txt_date.text = getString(R.string.refinance_date, it.date)
        txt_total_price.text = it.totalPrice

        refinance_image.loadImageByUrl("")
    }

    private fun initInstances() {
        refinance_note.movementMethod = LinkMovementMethod.getInstance()

        layout_active_refinance.title_staff_contact_active.movementMethod = LinkMovementMethod.getInstance()
        layout_active_refinance.title_staff_contact_active.setOnClickListener { AnalyticsManager.refinanceCallClicked() }

        layout_active_refinance.btn_interest.setOnClickListener {
            AnalyticsManager.refinanceInterestRefinanceClicked()
            RefinanceDialogFragment.show(fragmentManager!!)
        }

    }

    private fun supportForStaff(it: RefinanceViewModel.Model) {

    }

    private fun matchCondition() {
        layout_active_refinance.visible()
        layout_inactive_refinance.gone()
        refinance_note.visible()
    }

    private fun notMatchCondition() {
        layout_active_refinance.gone()
        layout_inactive_refinance.visible()
        refinance_note.gone()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        fun newInstance() = RefinanceFragment()
    }
}
