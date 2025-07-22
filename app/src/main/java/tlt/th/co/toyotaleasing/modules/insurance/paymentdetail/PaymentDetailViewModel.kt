package tlt.th.co.toyotaleasing.modules.insurance.paymentdetail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.db.CacheManager

class PaymentDetailViewModel : BaseViewModel() {

    private val insurance = CacheManager.getCacheInsurance()

    val whenDataLoaded = MutableLiveData<PaymentDetailViewModel.Model>()
    val whenDisplayUiByStatus = MutableLiveData<Status>()

    fun getData() {
        val data = Model(
                "${insurance?.cREGNO} - ${insurance?.cREGPROVINCE}",
                insurance?.cUSTNAME ?: "",
                insurance?.eXTCONTRACT ?: "",
                insurance?.cURRENTDATE?.toDatetime() ?: "",
                insurance?.iNSCOMNAME ?: "",
                insurance?.iTYPECOVERAGE ?: "",
                insurance?.iCOVERAGEAMT?.toNumberFormat() ?: "",
                insurance?.iREPAIRCON ?: "",
                insurance?.iEXPDATE?.toDatetime()
                        ?: "",
                insurance?.iSUMAMT?.toNumberFormat() ?: "",
                "Y"
        )
        whenDataLoaded.postValue(data)
        whenDisplayUiByStatus.postValue(data.status)
    }

    data class Model(val carLicense: String = "",
                     val fullname: String = "",
                     val contractNumber: String = "",
                     val currentDate: String = "",
                     val insuranceCompany: String = "",
                     val insuranceProtection: String = "",
                     val insuranceBudget: String = "",
                     val conditionRepair: String = "",
                     val endOfProtectionDate: String = "",
                     val insurancePremium: String = "",
                     val _status: String = "") {
        val status: Status
            get() {
                return when (_status) {
                    "Y" -> Status.PAYABLE
                    else -> Status.UNPAYABLE
                }
            }
    }

    enum class Status {
        PAYABLE,
        UNPAYABLE
    }
}