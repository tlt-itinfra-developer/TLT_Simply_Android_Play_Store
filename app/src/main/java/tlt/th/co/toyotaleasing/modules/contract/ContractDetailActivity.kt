package tlt.th.co.toyotaleasing.modules.contract

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_car_contract_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.showToast
import tlt.th.co.toyotaleasing.modules.contract.contract.ContractFragment
import tlt.th.co.toyotaleasing.modules.contract.payoff.PayoffFragment
import tlt.th.co.toyotaleasing.modules.contract.refinance.RefinanceFragment

class ContractDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ContractDetailViewModel::class.java)
    }

    private val defaultTabPosition by lazy {
        intent?.getIntExtra(TAB_SELECT_POSITION, 0) ?: 0
    }

    private val contractNumberExtra by lazy {
        intent?.getStringExtra(CONTRACT_NO_EXTRA) ?: ""
    }

    private val contractStatus by lazy {
        intent?.getStringExtra(CONTRACT_STATUS) ?: ""
    }

    private var lastTabPosition = defaultTabPosition

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_contract_detail)

        initViewModel()

        viewModel.getInstallment(contractNumberExtra)
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoadedFailure.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.whenDataLoadedSuccess.observe(this, Observer {
            initTabs()
        })
    }

    private fun initTabs() {
        tablayout.getTabAt(defaultTabPosition)?.select()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putInt(TAB_SELECT_POSITION, tablayout.selectedTabPosition)
    }

    companion object {
        const val CONTRACT_POSITION = 0
        const val PAYOFF_POSITION = 1
        const val REFINANCE_POSITION = 2

        private const val CONTRACT_NO_EXTRA = "CONTRACT_NO_EXTRA"
        private const val TAB_SELECT_POSITION = "tablayout_current_position"
        private const val CONTRACT_STATUS = "contract_status"

        fun start(context: Context?,
                  contractNumber: String = "",
                  position: Int = CONTRACT_POSITION,
                  contractStatus: String = "") {
            val intent = Intent(context, ContractDetailActivity::class.java).apply {
                putExtra(TAB_SELECT_POSITION, position)
                putExtra(CONTRACT_NO_EXTRA, contractNumber)
                putExtra(CONTRACT_STATUS, contractStatus)
            }

            context?.startActivity(intent)
        }
    }
}
