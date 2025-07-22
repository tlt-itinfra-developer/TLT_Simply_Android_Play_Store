package tlt.th.co.toyotaleasing.common.extension

import com.google.android.gms.common.util.Base64Utils
import tlt.th.co.toyotaleasing.util.NotificationConstants
import java.text.DecimalFormat

fun String.toNumberFormat(): String {
    if (this.isEmpty()) {
        return "0.00"
    }

    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(this.toDouble())
}

fun String.toNumberFormatWithoutComma(): String {
    if (this.isEmpty()) {
        return "0.00"
    }

    val formatter = DecimalFormat("0.00")
    return formatter.format(this.replace(",", "").toDouble())
}

fun String.toStringWithoutComma(): String {
    if (this.isEmpty()) {
        return "0"
    }

    return this.replace(",", "")
}

fun String.toNumberWithoutDecimal(): String {
    if (this.isEmpty()) {
        return ""
    }

    return this.toFloat().toInt().toString()
}

fun String.toWaitingViaAPI(): String {
    return "loading"
}


fun String.decodeBase64() = Base64Utils.decode(this)

fun String.getTypePasswordX(): String {
    var result = ""
    for (i in 0 until this.length) {
        result += "x"
    }
    return result
}

fun String.getTypeNotification(): String {
    return when {
        this.startsWith(NotificationConstants.TYPE_USER) -> NotificationConstants.USER_PAGE
        this.startsWith(NotificationConstants.TYPE_FAQ) -> NotificationConstants.FAQ_PAGE
        this.startsWith(NotificationConstants.TYPE_MYCAR) -> NotificationConstants.MYCAR_PAGE
        this.startsWith(NotificationConstants.TYPE_INSURANCE) -> NotificationConstants.INSURANCE_PAGE
        this.startsWith(NotificationConstants.TYPE_TAX) -> NotificationConstants.TAX_PAGE
        this.startsWith(NotificationConstants.TYPE_TIB) -> NotificationConstants.TIB_PAGE
        this.startsWith(NotificationConstants.TYPE_CAR_LOAN) -> NotificationConstants.CAR_LOAN
        else -> NotificationConstants.ETC
    }
}


