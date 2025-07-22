package tlt.th.co.toyotaleasing.util

import android.util.Log
import tlt.th.co.toyotaleasing.common.extension.ifTrue

object LogUtils {
    fun log(tag: String = "", message: String = "") {
        AppUtils.isDevEnvironment().ifTrue {
            Log.d(tag, message)
        }
    }
}