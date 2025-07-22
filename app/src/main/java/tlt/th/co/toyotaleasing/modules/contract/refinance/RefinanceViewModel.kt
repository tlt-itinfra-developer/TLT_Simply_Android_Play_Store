package tlt.th.co.toyotaleasing.modules.contract.refinance

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.util.AppUtils

class RefinanceViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenShowStatus = MutableLiveData<Status>()

    fun getDataRefinance() {
        getDataByDB()
    }

    private fun getDataByDB() {
        val selectedCar = CacheManager.getCacheInstallment()

        val data = Model(
                selectedCar?.cURRENTDATE?.toDatetime() ?: "",
                "${selectedCar?.cREGNO} - ${selectedCar?.cREGPROVINCE}",
                selectedCar?.cREFINANCEAMT?.toNumberFormat() ?: "",
                selectedCar?.flagRefinance ?: ""
        )

        whenDataLoaded.value = data
        whenShowStatus.value = data.status
    }

    data class Model(
            val date: String = "",
            val carLicense: String = "",
            val totalPrice: String = "",
            private val _status: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() = if (_status == "Y") {
                Status.MATCH_CONDITION
            } else {
                Status.NOT_MATCH_CONDITION
            }
    }

    enum class Status {
        MATCH_CONDITION,
        NOT_MATCH_CONDITION,
    }
}