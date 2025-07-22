package tlt.th.co.toyotaleasing.modules.insurance.hotline

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import tlt.th.co.toyotaleasing.modules.insurance.hotline.accident.AccidentFragment
import tlt.th.co.toyotaleasing.modules.insurance.hotline.contactus.ContactUsFragment
import tlt.th.co.toyotaleasing.modules.insurance.hotline.emergency.EmergencyFragment
import tlt.th.co.toyotaleasing.modules.insurance.hotline.service.ServiceFragment

class HotLineAdapter(supportFragmentManager: FragmentManager,
                     private var tabMenuList : MutableList<String>) : FragmentPagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AccidentFragment.newInstance()
            1 -> EmergencyFragment.newInstance()
//            2 -> ServiceFragment.newInstance()
            2 -> ContactUsFragment.newInstance()
            else -> AccidentFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return tabMenuList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabMenuList[position]
    }
}