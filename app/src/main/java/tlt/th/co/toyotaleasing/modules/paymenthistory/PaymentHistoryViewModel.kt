package tlt.th.co.toyotaleasing.modules.paymenthistory

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.getYears
import tlt.th.co.toyotaleasing.common.extension.toDateByPattern
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.GetPaymentHistoryRequest
import tlt.th.co.toyotaleasing.model.request.GetReceiptDocumentRequest
import tlt.th.co.toyotaleasing.model.response.GetPaymentHistoryResponse
import tlt.th.co.toyotaleasing.model.response.GetReceiptDocumentResponse
import tlt.th.co.toyotaleasing.model.response.ItemMyCarResponse
import tlt.th.co.toyotaleasing.util.PdfUtils

class PaymentHistoryViewModel : BaseViewModel() {

    val whenOpenPdf = MutableLiveData<String>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenLockData = MutableLiveData<Boolean>()
    val apiManager = TLTApiManager.getInstance()

    private val filterList by lazy {
        MasterDataManager.getInstance().getPayHistoryList()
    }

    private val paymentHistoryList = mutableListOf<GetPaymentHistoryResponse.PAYMENTHIS?>()

    fun getPaymentHistories(contractNumber: String = "", yearFilter: String = "") {
        val defaultCar = getCurrentCar(contractNumber)
        val request = GetPaymentHistoryRequest.build(defaultCar?.contractNumber ?: "")

//        if (whenDataLoaded.value?.carLicense == "${defaultCar?.regNumber} - ${defaultCar?.cREGPROVINCE}") {
//            return
//        }

        if (defaultCar!!.contractDescription == "other contract" ||
                defaultCar.contractDescription == "legal contract") {
            whenLockData.postValue(true)
        } else {
            apiManager.getPaymentHistory(request) { isError, result ->
                if (isError) {
                    return@getPaymentHistory
                }

                val item = JsonMapperManager.getInstance()
                        .gson.fromJson(result, GetPaymentHistoryResponse::class.java)

//            if (item.pAYMENTHIS?.isNotEmpty()!!) {
//                return@getPaymentHistory
//            }
                paymentHistoryList.clear()
                paymentHistoryList.addAll(item.pAYMENTHIS ?: listOf())

                val paymentHistories = item.pAYMENTHIS?.map {
                    PaymentHistory(
                            total = it?.rECEIPTAMT?.toNumberFormat() ?: "",
                            paymentDate = it?.receiveDate ?: "",
                            paymentReceiptDate = it?.receiptDate ?: "",
                            paymentBank = it?.bANKNME ?: "",
                            canDownloadReceipt = it?.rECEIPTSTSCODE?.toLowerCase() == "y",
                            year = it?.receiveDate!!.toDateByPattern().getYears()
                    )
                }!!.filter {
                    yearFilter.isEmpty() || yearFilter == it.year
                }

                val model = Model(
                        carLicense = "${defaultCar.regNumber} - ${defaultCar.cREGPROVINCE}",
                        date = defaultCar.cURRENTDATE?.toDatetime() ?: "",
                        filterList = filterList,
                        notifyList = paymentHistories ?: listOf(),
                        contractStatus = defaultCar.contractDescription!!
                )

                whenDataLoaded.postValue(model)
            }
        }
    }

    fun downloadReceipt(position: Int = 0) {
        paymentHistoryList.getOrNull(position)?.let {
            val filename = it.rECEIPTID ?: ""

//            if (PdfUtils.isFileExist(filename)) {
//                whenOpenPdf.postValue(filename)
//                return
//            }

            val request = GetReceiptDocumentRequest.build(
                    contractNumber = it.eXTCONTRACT ?: "",
                    documentNumber = it.dOCNBR ?: "",
                    receiptDate = it.receiptDate ?: ""
            )

            whenLoading.postValue(true)

            apiManager.getReceiptDocument(request) { isError, result ->
                whenLoading.postValue(false)

                if (isError) {
                    return@getReceiptDocument
                }

                val item = JsonMapperManager.getInstance()
                        .gson.fromJson(result, GetReceiptDocumentResponse::class.java)

                PdfUtils.savePdf(filename, item.dATA ?: "")
                whenOpenPdf.postValue(filename)
            }
        }
    }

    private fun getCurrentCar(defaultContractNumber: String = ""): ItemMyCarResponse? {
        return null
    }

    data class Model(
            val carLicense: String = "",
            val date: String = "",
            val filterList: List<String> = listOf(),
            val notifyList: List<PaymentHistory> = listOf(),
            val contractStatus: String = ""
    ) {
        val status: Status
            get() {
                return when (contractStatus.toLowerCase()) {
                    "other contract" -> Status.LOCK
                    "legal contract" -> Status.LOCK
                    else -> if (notifyList.isEmpty()) {
                        Status.NOT_FOUND
                    } else {
                        Status.FOUND
                    }
                }
            }
    }

    enum class Status {
        FOUND,
        NOT_FOUND,
        LOCK
    }
}