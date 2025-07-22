package tlt.th.co.toyotaleasing.modules.customer.mycar

import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.util.AppUtils

data class MyCar(
        val id: String = "",
        val license: String = "",
        val contractNumber: String = "",
        val carImageBase64: String = "",
        val defaultCarImageUrl: String = "",
        val currentDate: String = "",
        val totalTerm: String = "",
        val paidTerm: String = "",
        val _periodPayment: String = "",
        val totalPayment: String = "",
        val unpaidInstall: String = "",
        val flagPayProcess: String = "",
        val _contractStatus: String = "",
        val contractStatusDesc: String = "",
        val isStaffApp: Boolean = AppUtils.isStaffApp(),
        val flagDirectDebit: String = ""
) {

    val remainTerm: String
        get() {
            val remain = totalTerm.toFloat().toInt() - paidTerm.toFloat().toInt()
            return remain.toString()
        }

    val periodPayment: String
        get() {
            val dates = _periodPayment.split("-")

            if (dates.size > 1) {
                val dateBefore = dates[0].trim().toDatetime()
                val dateAfter = dates[1].trim().toDatetime()
                return "$dateBefore - $dateAfter"
            }

            return dates[0].trim().toDatetime()
        }

    val status: Status
        get() {
            return when (contractStatusDesc.toLowerCase()) {
                "closed contract" -> Status.CLOSE
                "legal contract" -> Status.LEGAL
                "other contract" -> Status.OTHER
                else -> if (flagDirectDebit.toLowerCase() == "y") {
                    Status.DIRECT_DEBIT
                } else {
                    if (contractStatusDesc.toLowerCase() == "normal" &&
                            flagPayProcess == "Y") {
                        Status.UPDATING
                    } else {
                        Status.NORMAL
                    }
                }
            }
        }

    enum class Status {
        NORMAL,
        UPDATING,
        CLOSE,
        OTHER,
        LEGAL,
        DIRECT_DEBIT
    }
}