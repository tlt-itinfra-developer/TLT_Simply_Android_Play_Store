package tlt.th.co.toyotaleasing.manager.analytic

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticManager private constructor() {

    private lateinit var googleFirebaseAnalytic: GoogleFirebaseAnalytic

    companion object {
        private val instance = AnalyticManager()

        fun getInstance(context: Context): AnalyticManager {
            instance.googleFirebaseAnalytic = GoogleFirebaseAnalytic.getInstance(context)
            return instance
        }
    }

    fun event() {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, "123")
            putString(FirebaseAnalytics.Param.ITEM_NAME, "TestItem")
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        }

        googleFirebaseAnalytic.event(bundle)
    }
}