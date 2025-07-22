package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import org.greenrobot.eventbus.Subscribe
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.eventbus.NoInternetEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.ContextManager

class NoInternetLifeCycleobserver(private val activity: AppCompatActivity) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        BusManager.subscribe(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        BusManager.unsubscribe(this)
    }

    @Subscribe
    fun onNoInternetReceived(event: NoInternetEvent) {
        Toast.makeText(activity.applicationContext, ContextManager.getInstance().getApplicationContext().resources.getString(R.string.alert_no_internet_title), Toast.LENGTH_SHORT).show()
    }

}