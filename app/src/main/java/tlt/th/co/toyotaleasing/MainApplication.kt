package tlt.th.co.toyotaleasing

//import com.crashlytics.android.Crashlytics
//import io.fabric.sdk.android.Fabric
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.github.ajalt.reprint.core.Reprint
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
//import me.yokeyword.fragmentation.Fragmentation
import tlt.th.co.toyotaleasing.common.lifecycleobserver.AppStateLifeCycleObserver
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.analytic.AnalyticManager

import tlt.th.co.toyotaleasing.manager.notification.AppNotificationManager


class MainApplication : MultiDexApplication(), Application.ActivityLifecycleCallbacks {

    private var currentActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initProviders()
    }

    private fun initProviders() {
        AndroidThreeTen.init(this)
        ContextManager.getInstance().setApplicationContext(this)
        Reprint.initialize(this)
        AppNotificationManager.subscribe("all", BuildConfig.FCM_TLT_TOPIC)
        AnalyticManager.getInstance(this)
        LocalizeManager.initDefaultLocalize(this)
        ContextManager.getInstance().setApplicationContext(LocalizeManager.initDefaultLocalize(this))
        //Fragmentation.builder().install()
        ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(AppStateLifeCycleObserver())
        setupCrashlytic()
    }

    private fun setupCrashlytic() {
        // Operations on Crashlytics.
       // val crashlytics = FirebaseCrashlytics.getInstance()
      //  crashlytics.setCrashlyticsCollectionEnabled(true)
//        val fabric = Fabric.Builder(this)
//                .kits(Crashlytics())
//                .debuggable(false)  // Enables Crashlytics debugger
//                .build()
//        Fabric.with(fabric)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    companion object {
        fun restart(baseContext: Context) {
            val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            baseContext.startActivity(intent)
        }
    }
}