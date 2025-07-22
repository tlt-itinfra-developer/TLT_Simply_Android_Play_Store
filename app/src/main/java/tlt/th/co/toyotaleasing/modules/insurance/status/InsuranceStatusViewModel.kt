package tlt.th.co.toyotaleasing.modules.insurance.status

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataTypeInsuranceRequest
import tlt.th.co.toyotaleasing.model.response.GetDataTypeInsuranceResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.insurance.CarInformationWidget
import tlt.th.co.toyotaleasing.view.insurance.InsuranceWidget

class InsuranceStatusViewModel : BaseViewModel() {

    val whenDataLoaded = SingleLiveData<Model>()

    private val apiManager = TLTApiManager.getInstance()
    private val insurance = CacheManager.getCacheInsurance()

    fun getData() {
        whenLoading.postValue(true)

        val request = GetDataTypeInsuranceRequest.buildForStatus(insurance?.eXTCONTRACT!!)

        apiManager.getDataTypeInsurance(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@getDataTypeInsurance
            }

            val insuranceInformation = JsonMapperManager.getInstance()
                    .gson.fromJson(result, GetDataTypeInsuranceResponse::class.java)

            val carInformationData = getCarInformationData()
            val insuranceWidgetData = getInsuranceInfomationData()
            val sentDate = insuranceInformation.status?.pOSTDATE?.toDatetime() ?: ""
            val trackingNo = insuranceInformation.status?.tRACKINGNO ?: ""

            val data = Model(carInformationData, insuranceWidgetData, sentDate, trackingNo)

            whenDataLoaded.postValue(data)
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

    data class Model(
            var carInformationData: CarInformationWidget.CarInformationData,
            var insuranceInfomationData: InsuranceWidget.InsuranceInfomationData,
            var sentDate: String,
            var trackingNo: String,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}