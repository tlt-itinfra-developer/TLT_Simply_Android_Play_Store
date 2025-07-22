package tlt.th.co.toyotaleasing.modules.insurance.information

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataTypeInsuranceRequest
import tlt.th.co.toyotaleasing.model.response.GetDataTypeInsuranceResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.insurance.CarInformationWidget
import tlt.th.co.toyotaleasing.view.insurance.InsuranceWidget

class InsuranceInformationViewModel : BaseViewModel() {

    private val apiManager = TLTApiManager.getInstance()
    private val insurance = CacheManager.getCacheInsurance()

    val whenDataLoaded = SingleLiveData<Model>()
    val whenDisplayUiByStatus = MutableLiveData<Status>()

    fun getData() {
        whenLoading.postValue(true)

        val request = GetDataTypeInsuranceRequest.buildForHistory(insurance?.eXTCONTRACT ?: "")

        apiManager.getDataTypeInsurance(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getDataTypeInsurance
            }

            val insuranceInformation = JsonMapperManager.getInstance()
                    .gson.fromJson(result, GetDataTypeInsuranceResponse::class.java)

            val carInformationData = getCarInformationData()
            val insuranceWidgetData = getInsuranceInfomationData()
            val expireDays = if (insurance?.iEXPDAY == "") {
                ""
            } else {
                "${insurance?.iEXPDAY?.toNumberFormatWithoutComma()?.toFloat()?.toInt()}"
            }

            val insuranceHistories = insuranceInformation.history?.map {
                InsuranceHistoryData(
                        it?.iNSCOMLOGO ?: "",
                        it?.pOLICYNO ?: "",
                        it?.iNSCOMNAME ?: "",
                        it?.cOVERATETYPE ?: "",
                        it?.cOVERAGEAMT?.toNumberFormat() ?: "",
                        it?.rEPAIRCON ?: "",
                        it?.eXPDATE?.toDatetime() ?: "")
            }

            val data = Model(carInformationData, insuranceWidgetData, expireDays, insuranceHistories!!)

            whenDataLoaded.postValue(data)
            whenDisplayUiByStatus.postValue(data.status)
        }
    }

    private fun getCarInformationData(): CarInformationWidget.CarInformationData {
        return CarInformationWidget.CarInformationData().apply {
            licenseCar = "${insurance?.cREGNO} - ${insurance?.cREGPROVINCE}"
            licenseOwnerName = insurance?.cUSTNAME ?: ""
            contractNumber = insurance?.eXTCONTRACT ?: ""
            currentDate = insurance?.cURRENTDATE?.toDatetime() ?: ""
        }
    }

    private fun getInsuranceInfomationData(): InsuranceWidget.InsuranceInfomationData {
        return InsuranceWidget.InsuranceInfomationData().apply {
            companyName = insurance?.iNSCOMNAME ?: ""
            endOfProtectionDate = insurance?.iEXPDATE?.toDatetime() ?: ""
            no = insurance?.iPOLICYNO ?: ""
            type = insurance?.iTYPECOVERAGE ?: ""
        }
    }

    data class Model(var carInformationData: CarInformationWidget.CarInformationData,
                     var insuranceInfomationData: InsuranceWidget.InsuranceInfomationData,
                     var expireDays: String = "",
                     var historyData: List<InsuranceHistoryData> = listOf(),
                     val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() {
                return if (historyData.isEmpty()) {
                    Status.NO_DATA
                } else {
                    Status.NORMAL
                }
            }
    }

    enum class Status {
        NO_DATA,
        NORMAL
    }
}
