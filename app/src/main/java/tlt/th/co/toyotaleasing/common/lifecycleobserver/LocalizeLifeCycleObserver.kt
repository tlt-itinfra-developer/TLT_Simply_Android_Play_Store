package tlt.th.co.toyotaleasing.common.lifecycleobserver

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.appcompat.app.AppCompatActivity
import tlt.th.co.toyotaleasing.manager.LocalizeManager

class LocalizeLifeCycleObserver(private val activity: AppCompatActivity) : LifecycleObserver {

    private lateinit var language: String

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        language = LocalizeManager.getLanguage()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (language != LocalizeManager.getLanguage()) {
            activity.recreate()
        }
    }
}