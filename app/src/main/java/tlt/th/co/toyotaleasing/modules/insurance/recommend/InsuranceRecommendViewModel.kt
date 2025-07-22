package tlt.th.co.toyotaleasing.modules.insurance.recommend

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataTypeInsuranceRequest
import tlt.th.co.toyotaleasing.model.response.GetDataInsuranceResponse
import tlt.th.co.toyotaleasing.model.response.GetDataTypeInsuranceResponse
import tlt.th.co.toyotaleasing.modules.insurance.information.InsuranceHistoryData
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.insurance.CarInformationWidget
import tlt.th.co.toyotaleasing.view.insurance.InsuranceWidget

class InsuranceRecommendViewModel : BaseViewModel() {

    val whenDataLoaded = SingleLiveData<Model>()
    val whenDisplayUiByStatus = SingleLiveData<Status>()

    private val apiManager = TLTApiManager.getInstance()
    private val insurance = CacheManager.getCacheInsurance()

    fun getData() {
        whenLoading.postValue(true)

        val request = GetDataTypeInsuranceRequest.buildForHistory(insurance?.eXTCONTRACT!!)
        apiManager.getDataTypeInsurance(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getDataTypeInsurance
            }

            val insuranceInformation = JsonMapperManager.getInstance()
                    .gson.fromJson(result, GetDataTypeInsuranceResponse::class.java)

            val carInformationData = getCarInformationData()
            val insuranceWidgetData = getInsuranceInfomationData()
            val insuranceRecommend = insuranceInformation.recommend?.firstOrNull()

            val insuranceHistories = insuranceInformation.recommend?.map {
                InsuranceHistoryData(
                        it?.iNSCOMLOGO ?: "",
                        it?.iPOLICYNO ?: "",
                        it?.iNSCOMNAME ?: "",
                        it?.cOVERATETYPE ?: "",
                        it?.cOVERAGEAMT?.toNumberFormat() ?: "",
                        it?.rEPAIRCON ?: "",
                        it?.eXPDATE?.toDatetime() ?: ""
                )
            }

            val data = InsuranceRecommendViewModel.Model(
                    carInformationData,
                    insuranceWidgetData,
                    insuranceHistories?.firstOrNull(),
                    insuranceRecommend?.iNSSUMAMT?.toNumberFormat() ?: "0.00",
                    insuranceData = insurance)

            whenDataLoaded.postValue(data)
            if (insurance.flag4M!!.toLowerCase() != "n") {
                whenDisplayUiByStatus.postValue(data.status)
            }
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
                     var historyData: InsuranceHistoryData?,
                     var totalPrice: String,
                     val _status: String = "",
                     val isStaffApp: Boolean = AppUtils.isStaffApp(),
                     val insuranceData: GetDataInsuranceResponse
    ) {
        val status: Status
            get() {
                return if (historyData == null || insuranceData.flag4M!!.toLowerCase() == "e") {
                    Status.NOT_FOUND_RECOMMEND
                } else {
                    Status.FOUND_RECOMMEND
                }
            }
    }

    enum class Status {
        FOUND_RECOMMEND,
        NOT_FOUND_RECOMMEND,
    }
}