package tlt.th.co.toyotaleasing.analytics

import android.os.Bundle
import tlt.th.co.toyotaleasing.common.extension.toDatetimeByPattern
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import java.util.*

fun Bundle.putBaseViewEventParam(startTime: Date, endTime: Date): Bundle {
    this.putString(AnalyticsConstants.EXT_CONTRACT, UserManager.getInstance().getProfile().token)
    this.putString(AnalyticsConstants.START_TIME, startTime.toDatetimeByPattern("dd/MM/yyyy HH:mm:ss"))
    this.putString(AnalyticsConstants.TIME, endTime.toDatetimeByPattern("dd/MM/yyyy HH:mm:ss"))
    this.putString(AnalyticsConstants.DURATION_TIME, getDurationTime(startTime, endTime))
    this.putString(AnalyticsConstants.LANGUAGE, LocalizeManager.getLanguage())
    this.putString(AnalyticsConstants.TYPE, AnalyticsConstants.TYPE_VIEW)
    this.putString(AnalyticsConstants.ACTION, "")
    this.putString(AnalyticsConstants.LABEL, "")
    this.putString(AnalyticsConstants.VALUE, "")
    this.putString(AnalyticsConstants.INFO, "")

    return this
}

fun Bundle.putBaseEventParam(action: String = "click"): Bundle {
    this.putString(AnalyticsConstants.TIME, Calendar.getInstance().time.toDatetimeByPattern("dd/MM/yyyy HH:mm:ss"))
    this.putString(AnalyticsConstants.EXT_CONTRACT, UserManager.getInstance().getProfile().token)
    this.putString(AnalyticsConstants.START_TIME, "")
    this.putString(AnalyticsConstants.DURATION_TIME, "")
    this.putString(AnalyticsConstants.LANGUAGE, LocalizeManager.getLanguage())
    this.putString(AnalyticsConstants.TYPE, AnalyticsConstants.TYPE_EVENT)
    this.putString(AnalyticsConstants.ACTION, action)

    return this
}

private fun getDurationTime(startTime: Date = Calendar.getInstance().time,
                            endTime: Date = Calendar.getInstance().time): String {
    return if (endTime.time > startTime.time) {
        val diffTime = endTime.time - startTime.time
        (diffTime / 1000).toInt().toString()
    } else {
        "1"
    }


}