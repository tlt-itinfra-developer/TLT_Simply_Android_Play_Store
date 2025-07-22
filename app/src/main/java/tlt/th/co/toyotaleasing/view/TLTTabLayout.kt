package tlt.th.co.toyotaleasing.view

import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.core.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import tlt.th.co.toyotaleasing.R

/**
 * Created by beer on 6/7/2018 AD.
on MAC TLT
 */

class TLTTabLayout : com.google.android.material.tabs.TabLayout {

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    fun changeTabsFont() {
        val vg = this.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                val tf = ResourcesCompat.getFont(context, R.font.rsu_bold)
                if (tabViewChild is TextView) {
                    tabViewChild.typeface = tf
                }
            }
        }
    }
}