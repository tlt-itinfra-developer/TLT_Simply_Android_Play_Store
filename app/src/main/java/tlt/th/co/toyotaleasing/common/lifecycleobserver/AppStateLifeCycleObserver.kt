package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import tlt.th.co.toyotaleasing.common.eventbus.SendAnalyticsEvent
import tlt.th.co.toyotaleasing.manager.BusManager


class AppStateLifeCycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onForeground() {
        ////DatabaseManager.getInstance().updateAppState(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        BusManager.observe(SendAnalyticsEvent())
       // //DatabaseManager.getInstance().updateAppState(false)
    }
}