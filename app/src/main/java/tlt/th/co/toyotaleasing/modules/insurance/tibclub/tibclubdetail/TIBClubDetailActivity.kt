package tlt.th.co.toyotaleasing.modules.insurance.tibclub.tibclubdetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_tib_club_detail.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.gone
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.loadImageByUrl
import tlt.th.co.toyotaleasing.util.ExternalAppUtils

class TIBClubDetailActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TIBClubDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tib_club_detail)

        initViewModel()

        viewModel.getData()
    }

    private fun initViewModel() {
        viewModel.whenDataLoaded.observe(this, Observer {
            it?.let {
                setupDataIntoViews(it)
                it.isStaffApp.ifTrue { supportForStaff(it) }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.trackScreen(AnalyticsScreenName.INSURANCE_TIB_CLUB_ACTIVITY)
    }

    private fun setupDataIntoViews(it: TIBClubDetailViewModel.Model?) {
        txt_event_title.text = it?.title
        txt_event_detail.text = it?.detail
        img_event.loadImageByUrl(it?.imageUrl!!)
        img_event.setOnClickListener {
            try {
                ExternalAppUtils.openByLink(this@TIBClubDetailActivity, "")
            } catch (e: Exception) {
                ExternalAppUtils.openByLink(this@TIBClubDetailActivity, "")
                e.printStackTrace()
            }
        }

        it.title.isEmpty().ifTrue {
            txt_event_title.gone()
        }

        it.detail.isEmpty().ifTrue {
            txt_event_detail.gone()
        }

        it.imageUrl.isEmpty().ifTrue {
            img_event.gone()
        }
    }

    private fun supportForStaff(it: TIBClubDetailViewModel.Model) {

    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, TIBClubDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}
