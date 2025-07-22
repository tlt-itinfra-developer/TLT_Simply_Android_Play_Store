package tlt.th.co.toyotaleasing.modules.tax

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataTaxRequest
import tlt.th.co.toyotaleasing.model.request.SetUpdateTaxYearRequest
import tlt.th.co.toyotaleasing.model.response.GetDataTaxResponse
import tlt.th.co.toyotaleasing.modules.tax.utils.TaxDataHelper
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.CheckStatusTaxWidget

class TaxViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenUpdateTaxSuccess = MutableLiveData<Boolean>()

    var isShowLockTaxDocument: Boolean = false

    private lateinit var taxDataHelper: TaxDataHelper
    private val apiTLTApiManager = TLTApiManager.getInstance()

    fun getTaxData(contractNumber: String = "") {
        whenLoading.postValue(true)

        val contractNumberRequest = if (contractNumber.isEmpty()) {
            CacheManager.getCacheCar()?.contractNumber ?: ""
        } else {
            contractNumber
        }

        if (AppUtils.isLocalhostEnvironment()) {
            /**
             * Mock Data API
             */
            val request = GetDataTaxRequest.build(contractNumberRequest)
            apiTLTApiManager.getDataTaxLocalhost(request) { isError, result ->
                whenLoading.postValue(false)
                if (isError) {
                    return@getDataTaxLocalhost
                }
                val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<GetDataTaxResponse>::class.java)
                if (items.isEmpty()) {
                    return@getDataTaxLocalhost
                }
                val item = items.first()
                CacheManager.cacheTax(item)
                taxDataHelper = TaxDataHelper(item)
                isShowLockTaxDocument = item.flagPayProcessTax?.toLowerCase() == "n" &&
                        item.flagSETTLEMENT?.toLowerCase() == "n"
                val statusDocument = getStatusDocument(item)
                val data = Model(
                        carLicense =  "${item.cREGNO} - ${item.cREGPROVINCE}",
                        fullname = item.cUSTNAME ?: "",
                        contractNumber = item.eXTCONTRACT ?: "",
                        taxExpire =  item.cTAXEXPDATE?.toDatetime() ?: "",
                        paymentDate =  item.cURRENTDATE?.toDatetime() ?: "",
                        amountTax = item.cTAXAMT?.toNumberFormat() ?: "",
                        serviceCharge = item.cSERVICEFEE?.toNumberFormat() ?: "",
                        aol=  item.cCOMPULSORYAMT?.toNumberFormat() ?: "",
                        penalty =  item.cTAXPENALTYAMT?.toNumberFormat() ?: "",
                        totalPayment =  item.cTAXSUMAMT?.toNumberFormat() ?: "",
                        addressToTransport = "${item.aDDRESSLINE1} ${item.aDDRESSLINE2} ${item.aDDRESSLINE3} ${item.aDDRESSLINE4} ${item.pOSTCODE}",
                        isShowHeaderWarning = taxDataHelper.isWarningDocument(),
                        isPayProcess =  item.flagPayProcessTax == "Y",
                        _status = item.cCONTRACTSTATUS ?: "" ,
                        statusDocument =  statusDocument,
                        dataTax = item ,
                        taxDes = "" ,
                        flagTracking = item.flagTracking?: ""
                )
                whenDataLoaded.postValue(data)
            }
        } else {

            val request = GetDataTaxRequest.build(contractNumberRequest)
            apiTLTApiManager.getDataTax(request) { isError, result ->
                whenLoading.postValue(false)
                if (isError) {
                    return@getDataTax
                }
                val items = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<GetDataTaxResponse>::class.java)
                if (items.isEmpty()) {
                    return@getDataTax
                }
                val item = items.first()
                CacheManager.cacheTax(item)
                taxDataHelper = TaxDataHelper(item)

                isShowLockTaxDocument = item.flagPayProcessTax?.toLowerCase() == "n" &&
                        item.flagSETTLEMENT?.toLowerCase() == "n"
                val statusDocument = getStatusDocument(item)
                val data = Model(
                        carLicense = "${item.cREGNO} - ${item.cREGPROVINCE}",
                        fullname  =  item.cUSTNAME ?: "",
                        contractNumber = item.eXTCONTRACT ?: "",
                        taxExpire = item.cTAXEXPDATE?.toDatetime() ?: "",
                        paymentDate = item.cURRENTDATE?.toDatetime() ?: "",
                        amountTax =  item.cTAXAMT?.toNumberFormat() ?: "",
                        serviceCharge =  item.cSERVICEFEE?.toNumberFormat() ?: "",
                        aol = item.cCOMPULSORYAMT?.toNumberFormat() ?: "",
                        penalty =  item.cTAXPENALTYAMT?.toNumberFormat() ?: "",
                        totalPayment =  item.cTAXSUMAMT?.toNumberFormat()  ?: "",
                        addressToTransport = "${item.aDDRESSLINE1} ${item.aDDRESSLINE2} ${item.aDDRESSLINE3} ${item.aDDRESSLINE4} ${item.pOSTCODE}",
                        isShowHeaderWarning = taxDataHelper.isWarningDocument(),
                        isPayProcess = item.flagPayProcessTax == "Y",
                        _status = item.cCONTRACTSTATUS ?: "",
                        statusDocument = statusDocument,
                        dataTax = item,
                        taxDes = item.taxDes?: "" ,
                        flagTracking = item.flagTracking?: ""
                )
                whenDataLoaded.postValue(data)
            }
        }
    }

    fun isPorlorborRequesting() = taxDataHelper.isPorlorborRequesting()

    private fun getStatusDocument(item: GetDataTaxResponse): CheckStatusTaxWidget.Model {
        val flagPorlorbor = item.flagPORLORBOR
        val flagTorloraor = if (item.cVEHICLEAGE!!.toFloat().toInt() > 7) {
            item.flagTORLORAOR
        } else {
            ""
        }
        val flagGas = item.flagGAS

        val porlorborStatus = getCheckTaxStatus(flagPorlorbor ?: "")
        val torloraorStatus = getCheckTaxStatus(flagTorloraor ?: "")
        val gasStatus = getCheckTaxStatus(flagGas ?: "")

        val isShowPaymentComplete = listOf(porlorborStatus, torloraorStatus, gasStatus).all {
            it == CheckStatusTaxWidget.Status.COMPLETE || it == CheckStatusTaxWidget.Status.HIDE
        }

        return CheckStatusTaxWidget.Model(
                date = item.cURRENTDATE?.toDatetime() ?: "",
                porlorborStatus = porlorborStatus,
                torloraorStatus = torloraorStatus,
                gasStatus = gasStatus,
                isShowPaymentComplete = isShowPaymentComplete
        )
    }

    private fun getCheckTaxStatus(flag: String) = when (flag) {
        "R" -> CheckStatusTaxWidget.Status.WARNING
        "P" -> CheckStatusTaxWidget.Status.PENDING
        "RE" -> CheckStatusTaxWidget.Status.WRONG
        "V" -> CheckStatusTaxWidget.Status.COMPLETE
        else -> CheckStatusTaxWidget.Status.HIDE
    }

    fun updateTaxYear(year: String) {
        whenLoading.postValue(true)
        val extContract = CacheManager.getCacheCar()?.contractNumber ?: ""

        val request = SetUpdateTaxYearRequest.build(
                extContract = extContract,
                year = year
        )

        apiTLTApiManager.updateTaxYear(request) { isError, result ->

            if (isError) {
                whenLoading.postValue(false)
                return@updateTaxYear
            }


            whenLoading.postValue(false)
            if (result == "success") {
                whenUpdateTaxSuccess.postValue(true)
            } else {
                whenUpdateTaxSuccess.postValue(false)
            }

        }
    }

    data class Model(
            val carLicense: String = "",
            val fullname: String = "",
            val contractNumber: String = "",
            val taxExpire: String = "",
            val paymentDate: String = "",
            val amountTax: String = "",
            val serviceCharge: String = "",
            val aol: String = "",
            val penalty: String = "",
            val totalPayment: String = "",
            val addressToTransport: String = "",
            val isShowHeaderWarning: Boolean = true,
            val isPayProcess: Boolean = false,
            private val _status: String = "",
            val statusDocument: CheckStatusTaxWidget.Model = CheckStatusTaxWidget.Model(),
            val dataTax: GetDataTaxResponse,
            val taxDes : String = "",
            val flagTracking : String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() {
                return if (dataTax.cVEHICLETYPE?.toLowerCase() == "normal") {
                    if (dataTax.contractStatusDesc?.toLowerCase() == "normal") {
                        when {
                            dataTax.flagTaxOver3 == "Y" -> Status.EXPIRE
                            dataTax.flagPayProcessTax == "Y" -> Status.UPDATE
                            dataTax.flagSETTLEMENT == "Y" -> Status.AFTER_PAYMENT
                            else -> {
                                if (totalPayment == "0.00") {
                                    Status.CLOSE
                                } else {
                                    Status.DUE_DATE
                                }
                            }
                        }
                    } else {
                        Status.LEGAL
                    }
                } else {
                    Status.OTHER
                }
            }
    }

    /**
     * DUE_DATE [Group1 Normal Contract Status]
     * CLOSE [Group2 Closed Contract Status]
     * LEGAL [Group3 Legal Contract Status]
     * OTHER [Group4 Other Contract Status]
     * AFTER_PAYMENT [Group1 Paid complete]
     * EXPIRE [Group1 OVER 3 YEARS]
     * UPDATE [Group1 Updating]
     */

    enum class Status {
        DUE_DATE,
        CLOSE,
        LEGAL,
        OTHER,
        AFTER_PAYMENT,
        EXPIRE,
        UPDATE
    }
}