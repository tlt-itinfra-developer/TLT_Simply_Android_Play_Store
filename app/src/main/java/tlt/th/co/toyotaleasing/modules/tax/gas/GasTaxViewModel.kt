package tlt.th.co.toyotaleasing.modules.tax.gas

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.UploadImagesRequest
import tlt.th.co.toyotaleasing.util.AppUtils

class GasTaxViewModel : BaseViewModel() {

    private val myTax = CacheManager.getCacheTax()
    private val apiManager = TLTApiManager.getInstance()

    val whenDataLoaded = MutableLiveData<Model>()
    val whenEmsVerifyFail = MutableLiveData<String>()
    val whenEmsVerifySuccess = MutableLiveData<Boolean>()

    fun getData() {
        val model = Model(
                currentDate = myTax?.cURRENTDATE?.toDatetime() ?: "",
                defaultEms = myTax?.eMSNO ?: "",
                status = getStatus()
        )

        whenDataLoaded.postValue(model)
    }

    fun verifyEMSNumber(emsNumber: String) {
        whenLoading.postValue(true)

        val request = UploadImagesRequest.buildForGas(
                contractNumber = myTax?.eXTCONTRACT ?: "",
                seqId = "",
                emsNo = emsNumber
        )

        apiManager.uploadImages(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                whenEmsVerifyFail.postValue(result)
                return@uploadImages
            }

            whenEmsVerifySuccess.postValue(true)
        }
    }

    private fun getStatus() = when (myTax?.flagGAS) {
        "P" -> Status.PENDING
        "RE" -> Status.WRONG
        else -> Status.NOT_ATTACH_DOCUMENT
    }

    data class Model(
            val currentDate: String = "",
            val defaultEms: String = "",
            val status: Status,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )

    enum class Status {
        NOT_ATTACH_DOCUMENT,
        PENDING,
        WRONG
    }
}