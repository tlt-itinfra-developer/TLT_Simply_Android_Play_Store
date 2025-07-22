package tlt.th.co.toyotaleasing.modules.tax.porlorbor

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.model.request.GetImageUploadRequest
import tlt.th.co.toyotaleasing.model.response.GetImageUploadResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.imagehistory.ImageHistoriesWidget

class PorlorborTaxViewModel : BaseViewModel() {

    private val paymentManager = PaymentManager
    private val myTax = CacheManager.getCacheTax()
    private val apiManager = TLTApiManager.getInstance()

    val whenDataLoaded = MutableLiveData<Model>()
    val whenImagesLoaded = MutableLiveData<List<ImageHistoriesWidget.History>>()

    fun getData(isPaymentFlow: Boolean) {
        getImageUploadApi()

        val status = if (isPaymentFlow) {
            Status.NOT_ATTACH_DOCUMENT
        } else {
            getStatus()
        }

        val model = Model(
                currentDate = myTax?.cURRENTDATE?.toDatetime() ?: "",
                isSkip = isPaymentFlow,
                status = status,
                defaultImage = paymentManager.getPorlorborDocuments() ?: listOf()
        )

        whenDataLoaded.postValue(model)
    }

    fun saveDocument(base64List: List<String> = listOf()) {
        paymentManager.savePorlorborDocs(base64List)
    }

    fun saveDocumentState(base64List: List<String> = listOf()) {
        paymentManager.saveStatePorlorborDocs(base64List)
    }

    private fun getImageUploadApi() {
        val request = GetImageUploadRequest.buildForPorlorbor(myTax?.eXTCONTRACT ?: "")

        apiManager.getImageUpload(request) { isError: Boolean, result: String ->
            if (isError) {
                return@getImageUpload
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetImageUploadResponse>::class.java)
                    .toList()

            if (items.isEmpty()) {
                return@getImageUpload
            }

            val histories = items.groupBy { it.uPDATEDATE }
                    .map {
                        val listBase64 = it.value.map { it.fILEATTACH ?: "" }
                        ImageHistoriesWidget.History(it.key ?: "", listBase64)
                    }

            whenImagesLoaded.postValue(histories)
        }
    }

    private fun getStatus() = when (myTax?.flagPORLORBOR) {
        "P" -> Status.PENDING
        "RE" -> Status.WRONG
        else -> Status.NOT_ATTACH_DOCUMENT
    }

    data class Model(
            val currentDate: String = "",
            val isSkip: Boolean = false,
            val status: Status = Status.PENDING,
            val defaultImage: List<String> = listOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    enum class Status {
        NOT_ATTACH_DOCUMENT,
        PENDING,
        WRONG
    }
}