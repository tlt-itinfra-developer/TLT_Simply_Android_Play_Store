package tlt.th.co.toyotaleasing.common.base

import androidx.appcompat.app.AppCompatDialogFragment
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import java.util.*

open class BaseDialogFragment : AppCompatDialogFragment() {

    override fun onResume() {
        super.onResume()
        AnalyticsManager.dialogStartTime = Calendar.getInstance().time
    }

    override fun onPause() {
        super.onPause()
        AnalyticsManager.dialogEndTime = Calendar.getInstance().time
    }

}