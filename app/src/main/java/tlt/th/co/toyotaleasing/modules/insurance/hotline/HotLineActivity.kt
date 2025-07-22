package tlt.th.co.toyotaleasing.modules.insurance.hotline

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_hotline.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.dialog.NormalDialogFragment

class HotLineActivity : BaseActivity(), NormalDialogFragment.Listener {

    val position: Int by lazy {
        intent?.getIntExtra(POSITION_EXTRA, ACCIDENT) ?: ACCIDENT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotline)
        initTablayout()
    }

    private fun initTablayout() {
        val hotlineMenuList = resources.getStringArray(R.array.hotline_menu_list).toMutableList()
        val hotlineTitleMenuList = resources.getStringArray(R.array.title_hotline_menu_list).toMutableList()

        hotline_viewpager.apply {
            adapter = HotLineAdapter(
                    supportFragmentManager,
                    hotlineMenuList
            )
        }

        hotline_tablayout.apply {
            setupWithViewPager(hotline_viewpager)
            changeTabsFont()
        }

        hotline_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> AnalyticsManager.tibClaimCenter()
                    1 -> AnalyticsManager.tibEmergencyCall()
                    2 -> AnalyticsManager.tibContactUs()
                   // 3 -> AnalyticsManager.insuranceContactDealerLocation()
                }
                val currentTitle = hotlineTitleMenuList[position]
                toolbar.setTitle(currentTitle)
            }
        })

        hotline_viewpager.apply {
            currentItem = position
        }
    }

    override fun onDialogConfirmClick() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDialogCancelClick() {

    }

    companion object {
        private const val POSITION_EXTRA = "position"

        const val ACCIDENT = 0
        const val EMERGENCY = 1
//        const val DEALER = 2
        const val CONTACT = 2

        fun start(context: Context?, position: Int) {
            val intent = Intent(context, HotLineActivity::class.java)
            intent.putExtra(POSITION_EXTRA, position)
            context?.startActivity(intent)
        }
    }
}