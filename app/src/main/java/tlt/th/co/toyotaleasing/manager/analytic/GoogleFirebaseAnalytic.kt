package tlt.th.co.toyotaleasing.manager.analytic

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class GoogleFirebaseAnalytic private constructor() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    companion object {
        private val instance = GoogleFirebaseAnalytic()

        fun getInstance(context: Context): GoogleFirebaseAnalytic {
            instance.firebaseAnalytics = FirebaseAnalytics.getInstance(context)
            return instance
        }
    }

    fun event(bundle: Bundle) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }
}