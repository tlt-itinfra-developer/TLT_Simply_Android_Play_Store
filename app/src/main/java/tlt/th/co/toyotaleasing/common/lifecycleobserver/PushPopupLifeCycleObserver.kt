package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AppCompatActivity
import org.greenrobot.eventbus.Subscribe
import tlt.th.co.toyotaleasing.common.eventbus.PushPopupEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.modules.home.customer.NoticeDialogFragment

class PushPopupLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.unsubscribe(this)
    }

    @Subscribe
    fun onPushPopupReceived(event: PushPopupEvent) {
        if (Screens.nonCustomer.contains(activity::class.java.simpleName)) {
            return
        }
    }
}