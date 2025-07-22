package tlt.th.co.toyotaleasing.modules.tax

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetTrackingRequest
import tlt.th.co.toyotaleasing.model.response.ItemTrackingResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.util.CalendarUtils

class TaxDocumentDeliveryViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<TaxDocumentDeliveryViewModel.Model>()

    fun getTaxDocumentDeliveryData() {
        whenLoading.value = true

        val car = CacheManager.getCacheTax()

        TLTApiManager.getInstance()
                .getTracking(GetTrackingRequest.build(car?.eXTCONTRACT
                        ?: "")) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        return@getTracking
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemTrackingResponse>::class.java)

                    val deliverStatus: Status = if (items.isNullOrEmpty() && car!!.flagPayProcessTax!!.toLowerCase() == "y") {
                        Status.PAY
                    } else if (items.first().flagHold!!.toLowerCase() == "y") {
                        Status.HOLD
                    } else if (items.isNotEmpty() && items.first().fINISHDATE.isNotEmpty() && items.first().tRACKINGNO.isNotEmpty()) {
                        Status.AFTER_PAY
                    } else {
                        Status.NOT_IN_PROCESS
                    }

                    if (items.isEmpty()) {
                        val data = Model(
                                deliveryDate = getCurrentDate(),
                                deliverStatus = deliverStatus,
                                thaipostTrackWebsite = "https://track.thailandpost.co.th/?lang=th" , //"https://thailandpost.online/",
                                addressToDelivery = "${car?.aDDRESSLINE1} ${car?.aDDRESSLINE2} ${car?.aDDRESSLINE3} ${car?.aDDRESSLINE4} ${car?.pOSTCODE}",
                                holdMsg =  "",
                                holdDate = "",
                                holdDesc = ""
                        )
                        whenDataLoaded.postValue(data)
                    } else {
                        val item = items.first()
                        val data = Model(
                                item.currentDate.toDatetime(),
                                deliverStatus,
                                item.rECIEVEDATE.toDatetime(),
                                item.fINISHDATE.toDatetime(),
                                item.tRACKINGNO,
                                "https://track.thailandpost.co.th/?lang=th",
                                "${item.mAILINGADR1} ${item.mAILINGADR2} ${item.mAILINGADR3} ${item.mAILINGADR4} ${item.mAILINGPOST}",
                                holdMsg = items.first().holdMsg?: "",
                                holdDate = items.first().holdDate!!.toDatetime(),
                                holdDesc = items.first().holdDesc?: ""
                        )
                        whenDataLoaded.postValue(data)
                    }
                }
    }

    private fun getCurrentDate(): String {
        val currentDate = CalendarUtils.getCurrentDateByPattern("dd/MM/yyyy HH:mm:ss")
        return currentDate.toDatetime()
    }

    data class Model(
            val deliveryDate: String = "",
            val deliverStatus: Status = Status.NOT_IN_PROCESS,
            val inBetweenProceed: String = "",
            val transportTaxLabel: String = "",
            val registerNumber: String = "",
            val thaipostTrackWebsite: String = "",
            val addressToDelivery: String = "",
            val holdMsg: String = "",
            val holdDate: String = "",
            val holdDesc: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    enum class Status {
        NOT_IN_PROCESS,
        PAY,
        AFTER_PAY,
        HOLD
    }
}