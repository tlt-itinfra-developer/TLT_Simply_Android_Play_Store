package tlt.th.co.toyotaleasing.common.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AppCompatActivity
import tlt.th.co.toyotaleasing.common.eventbus.SendAnalyticsEvent
import tlt.th.co.toyotaleasing.common.lifecycleobserver.Screens
import tlt.th.co.toyotaleasing.manager.BusManager

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.checkmasterdata.CheckVersionMasterActivity
import tlt.th.co.toyotaleasing.modules.checkstatusfrombank.CheckStatusFromBankActivity

class ResumingAppLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val isAppStateForeground = false
        val isSocialLoginState = false
        val userManager = UserManager.getInstance()

        if (isAppStateForeground) {
            return
        }

        if (!userManager.isCustomer()
                || Screens.nonCustomer.contains(activity::class.java.simpleName)) {
            if (!Screens.mainActivity.contains(activity::class.java.simpleName)) {
                if (!isSocialLoginState) {
                    CheckVersionMasterActivity.start(activity)
                }
            }
            return
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        UserManager.getInstance().updateLastActive()
    }

}