package tlt.th.co.toyotaleasing.modules.contract.payoff

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_payoff.*
import kotlinx.android.synthetic.main.layout_account_closing_active.*
import kotlinx.android.synthetic.main.layout_account_closing_active.view.*
import kotlinx.android.synthetic.main.layout_account_closing_inactive.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.modules.contract.refinance.RefinanceDialogFragment
import tlt.th.co.toyotaleasing.modules.payment.cart.installment.CartInstallmentActivity

class PayoffFragment : BaseFragment() {

    private val SCROLL_POSITION_STATE = "scroll_position"

    private val contractStatus by lazy {
        arguments?.getString(CONTRACT_STATUS) ?: ""
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(PayoffViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payoff, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let {
            nestedscrollview.scrollY = savedInstanceState.getInt(SCROLL_POSITION_STATE, 0)
        }
    }

    private fun initViewModel() {
        viewModel.contractStatus = contractStatus

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenShowStatus.observe(this, Observer {
            when (it) {
                PayoffViewModel.Status.ACTIVE_CAN_REFINANCE -> activeCanRefinanceStatus()
                PayoffViewModel.Status.ACTIVE_CAN_NOT_REFINANCE -> activeCanNotRefinanceStatus()
                PayoffViewModel.Status.INACTIVE -> inactiveStatus()
            }
        })
    }

    private fun initInstances() {
        title_staff_contact_active.movementMethod = LinkMovementMethod.getInstance()
        title_staff_contact_inactive.movementMethod = LinkMovementMethod.getInstance()
        layout_account_closing_active.title_staff_contact_active.movementMethod = LinkMovementMethod.getInstance()

        title_staff_contact_active.setOnClickListener { AnalyticsManager.payoffCallClicked() }
        title_staff_contact_inactive.setOnClickListener { AnalyticsManager.payoffCallClicked() }
        layout_account_closing_active.title_staff_contact_active.setOnClickListener { AnalyticsManager.payoffCallClicked() }

        btn_interest.setOnClickListener {
            AnalyticsManager.payoffInterestFinanceClicked()
            RefinanceDialogFragment.show(fragmentManager!!)
        }



        btn_payoff.setOnClickListener {
            CartInstallmentActivity.startByPayoff(this!!.context!!, isPayoff = true)
        }
    }

    private fun setupDataIntoViews(it: PayoffViewModel.Model) {
        txt_date.text = getString(R.string.account_closing_date, it.date)
        txt_total_price.text = it.totalPrice
        if(it._status_payoff.toLowerCase() == "y" ){

            btn_active_account_closing.background = ContextCompat.getDrawable(this!!.context!!, R.drawable.selector_btn_circle)
            btn_active_account_closing.isEnabled = true
            divider.visible()
            btn_payoff.visible()
            if(it.cust_status == "41" ){
                title_payoff_description.visible()
            }else{
                title_payoff_description.gone()
            }

            if(it._status_PayoffProcess.toLowerCase() == "y"){
                title_payoff_description.gone()
                btn_payoff.gone()
                btn_active_account_closing.background = ContextCompat.getDrawable(this!!.context!!, R.drawable.bg_circle_active_account_closing)
            }else{
                btn_active_account_closing.background = ContextCompat.getDrawable(this!!.context!!, R.drawable.selector_btn_circle)
                title_payoff_description.visible()
                btn_payoff.visible()
            }

        }else{
            title_payoff_description.gone()
            btn_active_account_closing.background = ContextCompat.getDrawable(this!!.context!!, R.drawable.bg_circle_active_account_closing)
            btn_active_account_closing.isEnabled = false
            btn_payoff.gone()
        }
    }

    private fun supportForStaff(it: PayoffViewModel.Model) {

    }

    private fun activeCanRefinanceStatus() {
        layout_account_closing_active.visible()
        layout_account_closing_inactive.gone()
    }

    private fun activeCanNotRefinanceStatus() {
        layout_account_closing_active.visible()
        layout_account_closing_inactive.gone()
//        title_staff_contact_active.gone()
        btn_interest.gone()
    }

    private fun inactiveStatus() {
        layout_account_closing_active.gone()
        layout_account_closing_inactive.visible()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCROLL_POSITION_STATE, nestedscrollview.scrollY)
    }

    companion object {
        private const val CONTRACT_STATUS = "contractStatus"

        fun newInstance(status: String) = PayoffFragment().apply {
            arguments = Bundle().apply {
                putString(CONTRACT_STATUS, status)
            }
        }

    }
}
