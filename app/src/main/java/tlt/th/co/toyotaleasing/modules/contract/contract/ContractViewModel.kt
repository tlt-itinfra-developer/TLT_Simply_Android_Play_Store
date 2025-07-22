package tlt.th.co.toyotaleasing.modules.contract.contract

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.util.AppUtils

class ContractViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenShowContractStatus = MutableLiveData<Status>()

    fun getDataInstallment() {
        getDataByDB()
    }

    private fun getDataByDB() {
        val selectedCar = CacheManager.getCacheInstallment()!!
        val dealerName = if (LocalizeManager.isThai()) {
            selectedCar.dEALERNAMETH
        } else {
            selectedCar.dEALERNAMEEN
        }

        val address1 = if (selectedCar.aDDRESSLINE1.isNullOrEmpty() && selectedCar.aDDRESSLINE2.isNullOrEmpty()) {
            ""
        } else {
            "\n${selectedCar.aDDRESSLINE1} ${selectedCar.aDDRESSLINE2}"
        }
        val address2 = if (selectedCar.aDDRESSLINE3.isNullOrEmpty()) {
            ""
        } else {
            "\n${selectedCar.aDDRESSLINE3}"
        }
        val address3 = if (selectedCar.aDDRESSLINE4.isNullOrEmpty() && selectedCar.pOSTCODE.isNullOrEmpty()) {
            ""
        } else {
            "\n${selectedCar.aDDRESSLINE4} ${selectedCar.pOSTCODE}"
        }

        val data = Model(
                selectedCar.cURRENTDATE.toDatetime(),
                "${selectedCar.cREGNO} - ${selectedCar.cREGPROVINCE}",
                selectedCar.cUSTNAME,
                selectedCar.eXTCONTRACT,
                selectedCar.cVEHICLEMODEL,
                selectedCar.cCONTRACTDATE.toDatetime(),
                selectedCar.paymentduedate(),
                selectedCar.cINSTALLAMT.toNumberFormat(),
                "${selectedCar.cPAIDTERM.toFloat().toInt()}/${selectedCar.cTOTALTERM.toFloat().toInt()}",
                selectedCar.cUNPAIDINSTALL.toFloat().toInt().toString(),
                selectedCar.cUNPAIDINSTALLAMT.toNumberFormat(),
                selectedCar.cTOTALPAIDAMT.toNumberFormat(),
                selectedCar.cOUTSTANDINGAMT.toNumberFormat(),
                selectedCar.cPENALTY.toNumberFormat(),
                selectedCar.mOBILENO,
                selectedCar.eMAIL,
                "$address1 $address2 $address3",
                selectedCar.cTUCPCARPRICE.toNumberFormat(),
                dealerName,
                selectedCar.dEALERIMAGE1,
                selectedCar.cCONTRACTSTATUS,
                selectedCar.contractDescription,
                selectedCar.dEALERCODE,
                selectedCar.followupfee
        )

        whenDataLoaded.value = data
        whenShowContractStatus.value = data.status
    }

    data class Model(
            val date: String = "",
            val carLicense: String = "",
            val fullname: String = "",
            val carNo: String = "",
            val carModel: String = "",
            val contractDate: String = "",
            val dueDate: String = "",
            val paymentPerInstallment: String = "",
            val installmentPeriod: String = "",
            val amountUnpaidInstallments: String = "",
            val unpaidInstallments: String = "",
            val totalPaidInstallment: String = "",
            val balance: String = "",
            val latePaymentPenalty: String = "",
            val customerPhoneNumber: String = "",
            val customerEmail: String = "",
            val customerAddress: String = "",
            val suggestNewCar: String = "",
            val toyotaPlace: String = "",
            val imageToyotaPlaceUrl: String = "",
            private val _status: String = "",
            val contractDescription: String = "",
            val dealerCode: String = "",
            val followupfee: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() {
                return when (contractDescription.toLowerCase()) {
                    "normal" -> Status.NORMAL
                    "closed contract" -> Status.CLOSED
                    "legal contract" -> Status.LEGAL
                    else -> Status.OTHER
                }
            }
    }

    enum class Status {
        NORMAL,
        DEBT,
        CLOSED,
        LEGAL,
        OTHER
    }
}