package tlt.th.co.toyotaleasing.modules.editaddress.taxdelivery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_edit_tax_delivery_address.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.common.base.BaseActivity
import tlt.th.co.toyotaleasing.common.extension.ifFalse

class EditTaxDeliveryAddressActivity : BaseActivity() {

    private val TAB_SELECT_POSITION_STATE = "tablayout_current_position"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tax_delivery_address)

        initTabs()
    }

    private fun initTabs() {
        tablayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                return
            }

            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                return
            }

            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putInt(TAB_SELECT_POSITION_STATE, tablayout.selectedTabPosition)
    }

    companion object {
        fun open(context: Context) {
            val intent = Intent(context, EditTaxDeliveryAddressActivity::class.java)
            context.startActivity(intent)
        }
    }
}
