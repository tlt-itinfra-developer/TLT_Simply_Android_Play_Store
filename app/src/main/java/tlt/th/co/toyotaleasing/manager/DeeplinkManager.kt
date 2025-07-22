package tlt.th.co.toyotaleasing.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class DeeplinkManager private constructor() {

    private val DEEPLINK_PARAM_SCREEN_NAME = "screen_name"
    private val DEEPLINK_PARAM_VERIFY = "verify"
    private val PARAM_SCREEN_NAME = "screen_name"
    private val PARAM_VERIFY = "verify"

    fun cacheDeeplinkDataIfComing(activity: AppCompatActivity) {
        activity.intent.data?.let {
            val sharedPreferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
            val screenName = it.getQueryParameter(PARAM_SCREEN_NAME)
            val verify = it.getQueryParameter(PARAM_VERIFY)

            sharedPreferences.edit().putString(DEEPLINK_PARAM_SCREEN_NAME, screenName).apply()
            sharedPreferences.edit().putString(DEEPLINK_PARAM_VERIFY, verify).apply()
        }
    }

    fun isOpenVerifyEmailSuccess(activity: AppCompatActivity): Boolean {
        val sharedPreferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val screenName = sharedPreferences.getString(DEEPLINK_PARAM_SCREEN_NAME, "")
        val verify = sharedPreferences.getString(DEEPLINK_PARAM_VERIFY, "")
        return screenName!!.isNotEmpty() && screenName == "register" && verify == "true"
    }

    fun isOpenVerifyEmailFailure(activity: AppCompatActivity): Boolean {
        val sharedPreferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        val screenName = sharedPreferences.getString(DEEPLINK_PARAM_SCREEN_NAME, "")
        val verify = sharedPreferences.getString(DEEPLINK_PARAM_VERIFY, "")
        return screenName!!.isNotEmpty() && screenName == "register" && verify == "fail"
    }

    fun clearDeeplinkData(activity: AppCompatActivity) {
        val sharedPreferences = activity.getSharedPreferences(activity.packageName, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(DEEPLINK_PARAM_SCREEN_NAME).apply()
        sharedPreferences.edit().remove(DEEPLINK_PARAM_VERIFY).apply()
    }

    companion object {
        private val jsonMapperManager = DeeplinkManager()
        fun getInstance() = jsonMapperManager
    }
}