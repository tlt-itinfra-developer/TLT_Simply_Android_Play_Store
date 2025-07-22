package tlt.th.co.toyotaleasing.modules.newsandpromotion

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.modules.newsandpromotion.news.NewsFragment
import tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice.OilPriceFragment
import tlt.th.co.toyotaleasing.modules.newsandpromotion.oilprice.OilPriceWebFragment
import tlt.th.co.toyotaleasing.modules.newsandpromotion.traffic.TrafficFragment

class NewsFragmentPagerAdapter(supportFragmentManager: FragmentManager, val nameList: Array<String>) : FragmentPagerAdapter(supportFragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment.newInstance()
            1 -> OilPriceWebFragment.newInstance() // OilPriceFragment.newInstance()
            else -> TrafficFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return nameList[position]
    }

}