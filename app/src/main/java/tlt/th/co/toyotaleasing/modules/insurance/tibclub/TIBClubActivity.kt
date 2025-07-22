package tlt.th.co.toyotaleasing.modules.insurance.tibclub

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tib_club.*
import kotlinx.android.synthetic.main.layout_lottie_loading_screen.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.visible
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.modules.insurance.tibclub.tibclubdetail.TIBClubDetailActivity
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class TIBClubActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TIBClubViewModel::class.java)
    }

    private val tibCodeExtra by lazy {
        intent?.getStringExtra(TIB_CODE_EXTRA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tib_club)

        initViewModel()
        initInstances()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenLoading.observe(this, Observer {
            toggleLoadingScreenDialog(it!!)
        })

        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                val adapter = recycler_view.adapter as TIBClubAdapter
                adapter.updateItems(it)

                tibCodeExtra?.let { tibcode ->
                    startTIBClubDetailByTIBCode(tibcode)
                }

                viewModel.isStaffApp().ifTrue { supportForStaff(it) }
            }
        })

        viewModel.whenSingleDetailAlready.observe(this, Observer {
            if (it!!.isNotEmpty()) {
                CacheManager.cacheTIB(it[0]!!)
                TIBClubDetailActivity.open(this@TIBClubActivity)
            }
        })
    }

    private fun initInstances() {
        btn_line.setOnClickListener {
            AnalyticsManager.insuranceTibClubLine()
            try {
                ExternalAppUtils.openByLink(this@TIBClubActivity, "")
            } catch (e: Exception) {
                ExternalAppUtils.openByLink(this@TIBClubActivity, "")
            }
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(this@TIBClubActivity)
            adapter = TIBClubAdapter(arrayListOf(), onEventClickListener)
        }
    }

    private fun supportForStaff(it: ArrayList<TIBClubViewModel.Model>) {

    }

    private fun startTIBClubDetailByTIBCode(tibCode: String) {
        viewModel.selectTIBClubDetail(tibCode)
    }

    private val onEventClickListener = object : TIBClubAdapter.OnEventClickListener {
        override fun onClick(item: TIBClubViewModel.Model) {
            AnalyticsManager.insuranceTibClubActivity()
            startTIBClubDetailByTIBCode(item.id)
        }
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSURANCE_TIB_CLUB)
    }

    companion object {
        const val TIB_CODE_EXTRA = "TIB_CODE_EXTRA"

        fun start(context: Context?) {
            val intent = Intent(context, TIBClubActivity::class.java)
            context?.startActivity(intent)
        }

        fun startByDeeplink(context: Context?,
                            tibCode: String) {
            val intent = Intent(context, TIBClubActivity::class.java)
            intent.putExtra(TIB_CODE_EXTRA, tibCode)
            context?.startActivity(intent)
        }
    }
}
