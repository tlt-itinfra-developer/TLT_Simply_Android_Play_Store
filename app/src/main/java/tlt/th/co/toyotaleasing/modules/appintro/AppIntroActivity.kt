package tlt.th.co.toyotaleasing.modules.appintro

import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.activity_app_intro.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseActivity

class AppIntroActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(AppIntroViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_intro)

        initInstances()
    }

    override fun onPause() {
        super.onPause()
        when (recycler_view.currentPosition) {
            0 -> AnalyticsManager.trackScreen(AnalyticsScreenName.TUTORIAL_PAGE_1)
            1 -> AnalyticsManager.trackScreen(AnalyticsScreenName.TUTORIAL_PAGE_2)
            2 -> AnalyticsManager.trackScreen(AnalyticsScreenName.TUTORIAL_PAGE_3)
        }
    }

    private fun initInstances() {
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recycler_view)

        val itemList = ArrayList<AppIntro>()
        val appIntro1 = AppIntro(getString(R.string.appintro_tutorial_1_title),
                getString(R.string.appintro_tutorial_1_description),
                R.drawable.bg_tutorial_1,
                R.drawable.ic_appintro_mobile)
        val appIntro2 = AppIntro(getString(R.string.appintro_tutorial_2_title),
                getString(R.string.appintro_tutorial_2_description),
                R.drawable.bg_tutorial_2,
                R.drawable.ic_appintro_search)
        val appIntro3 = AppIntro(getString(R.string.appintro_tutorial_3_title),
                getString(R.string.appintro_tutorial_3_description),
                R.drawable.bg_tutorial_3,
                R.drawable.ic_appintro_chat)

        itemList.add(appIntro1)
        itemList.add(appIntro2)
        itemList.add(appIntro3)

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.adapter = AppIntroAdapter(itemList)

        //indicator.attachToRecyclerView(recycler_view)

        btn_tutorial_skip.setOnClickListener {
            sendAnalytics(recycler_view.currentPosition)
            viewModel.disableShowForNextOpenApp()
            finish()
        }
    }

    private fun sendAnalytics(position: Int) {
        when (position) {
            0 -> AnalyticsManager.tutorialScreen1()
            1 -> AnalyticsManager.tutorialScreen2()
            2 -> AnalyticsManager.tutorialScreen3()
        }
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, AppIntroActivity::class.java)
            context.startActivity(intent)
        }
    }
}
