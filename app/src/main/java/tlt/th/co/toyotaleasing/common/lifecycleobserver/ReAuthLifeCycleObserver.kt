package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AppCompatActivity

import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.modules.pincode.AuthPincodeActivity

class ReAuthLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        val isAppStateForeground = false
        val userManager = UserManager.getInstance()

        if (!userManager.isCustomer()
                || Screens.nonCustomer.contains(activity::class.java.simpleName)
                || isAppStateForeground
                || !userManager.isLastActiveOverLimit5mins()) {
            return
        }

        AuthPincodeActivity.startWithResult(activity)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        UserManager.getInstance().updateLastActive()
    }
}