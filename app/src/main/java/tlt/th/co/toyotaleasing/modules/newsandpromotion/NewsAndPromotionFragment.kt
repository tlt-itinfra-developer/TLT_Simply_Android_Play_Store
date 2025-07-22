package tlt.th.co.toyotaleasing.modules.newsandpromotion

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_news_and_promotion.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseFragment
import tlt.th.co.toyotaleasing.common.listener.OnHambergerClickListener
import tlt.th.co.toyotaleasing.manager.ContextManager

class NewsAndPromotionFragment : BaseFragment() {
    private val onHambergerClickListener by lazy {
        context as OnHambergerClickListener
    }


    private val nameList = arrayOf(ContextManager.getInstance().getApplicationContext().resources.getString(R.string.news_toolbar),
            ContextManager.getInstance().getApplicationContext().resources.getString(R.string.oil_price_toolbar),
            ContextManager.getInstance().getApplicationContext().resources.getString(R.string.traffic_toolbar))

    private val position: Int by lazy {
        arguments?.getInt(POSITION_EXTRA, 0) ?: 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_and_promotion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstances()
        initTabLayout()
    }

    private fun initInstances() {
        toolbar.setOnHambergerMenuClickListener {
            AnalyticsManager.newsAndPromotionListMenuHome()
            onHambergerClickListener.onHambergerClick()
        }
    }

    private fun initTabLayout() {
        viewpager.apply {
            adapter = NewsFragmentPagerAdapter(childFragmentManager, nameList = nameList)
            offscreenPageLimit = 5
        }

        news_tablayout.apply {
            setupWithViewPager(viewpager)
            changeTabsFont()
        }

        news_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when {
                    tab!!.position == 0 -> { AnalyticsManager.newsAndPromotionListNewsAndPromotionList() }
                    tab.position == 1 -> { AnalyticsManager.newsAndPromotionListNewsAndPromotionOil() }
                    else -> { AnalyticsManager.newsAndPromotionListNewsAndPromotionTraffic() }
                }
                toolbar.setTitle(nameList[tab.position])
            }

        })

        viewpager.apply {
            currentItem = position
        }
    }

    companion object {
        private const val POSITION_EXTRA = "position"

        fun newInstance() = NewsAndPromotionFragment()
    }
}