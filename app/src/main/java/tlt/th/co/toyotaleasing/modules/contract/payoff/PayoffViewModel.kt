package tlt.th.co.toyotaleasing.modules.contract.payoff

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.util.AppUtils

class PayoffViewModel : BaseViewModel() {

    var contractStatus: String = ""
    val whenDataLoaded = MutableLiveData<Model>()
    val whenShowStatus = MutableLiveData<Status>()

    fun getData() {
        getDataByDB()
    }

    private fun getDataByDB() {
        val selectedCar = CacheManager.getCacheInstallment()

        val data = Model(
                selectedCar?.cURRENTDATE?.toDatetime() ?: "",
                selectedCar?.cTOTALPAYOFFAMT?.toNumberFormat() ?: "",
                selectedCar?.flagPayOff ?: "",
                selectedCar?.flagPayoffProcess ?: "",
                selectedCar?.flagRefinance ?: "",
                contractStatus ,
                selectedCar?.cCONTRACTSTATUS?: ""
        )

        whenDataLoaded.value = data
        whenShowStatus.value = data.status
    }

    data class Model(
            val date: String = "",
            val totalPrice: String = "",
            val _status_payoff: String = "",
            val _status_PayoffProcess : String = "",
            private val canRefinance: String = "",
            private val contract_status: String = "",
            val cust_status : String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() =
                if (contract_status.toLowerCase() == "normal") {
                    if (_status_payoff.toLowerCase() == "y") {
                        if (canRefinance.toLowerCase() == "y") {
                            Status.ACTIVE_CAN_REFINANCE
                        } else {
                            Status.ACTIVE_CAN_NOT_REFINANCE
                        }
                    } else {
                        Status.INACTIVE
                    }
                } else {
                    Status.INACTIVE
                }
    }

    enum class Status {
        ACTIVE_CAN_REFINANCE,
        ACTIVE_CAN_NOT_REFINANCE,
        INACTIVE
    }
}