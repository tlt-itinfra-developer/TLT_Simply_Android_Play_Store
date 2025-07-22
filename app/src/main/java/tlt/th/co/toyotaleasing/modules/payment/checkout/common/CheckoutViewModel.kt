package tlt.th.co.toyotaleasing.modules.payment.checkout.common

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.model.entity.SequenceTransactionEntity
import tlt.th.co.toyotaleasing.model.entity.payment.PaymentEntity
import tlt.th.co.toyotaleasing.model.request.CheckStatusFromBankRequest
import tlt.th.co.toyotaleasing.model.request.SequenceIdRequest
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.util.AppUtils

class CheckoutViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()
    val whenBarcodeIsOpening = MutableLiveData<Boolean>()
    val whenIBankIsOpening = MutableLiveData<String>()
    val whenSubmitButtonStateChanged = MutableLiveData<Boolean>()
    val whenPushReceiptPage = MutableLiveData<CheckStatusFromBankResponse>()

    private val currentCar = CacheManager.getCacheCar()
    private val paymentManager = PaymentManager
    private val masterDataManager = MasterDataManager.getInstance()
    private val BARCODE_ID = "099"
    private val apiTLTApiManager = TLTApiManager.getInstance()

    fun getPayment(isShowFileAttach: Boolean = false) {
        val date = currentCar?.cURRENTDATE?.toDatetime() ?: ""
        val carNo = "${currentCar?.regNumber} - ${currentCar?.cREGPROVINCE}"
        val contractNumber = currentCar?.contractNumber ?: ""

        whenSubmitButtonStateChanged.postValue(false)
    }

    fun selectPaymentMethod(position: Int = 0, item: Model.PaymentMethodModel) {
        if(item.callBankDesc == "MBAPI" || (item.callBankDesc != "MBAPI" && item.channel == "MBANK")){
            whenDataLoaded.value?.wallet?.forEach {
                it.isSelected = false
            }

            whenDataLoaded.value?.wallet?.getOrNull(position)?.apply {
                isSelected = true
            }

            whenSubmitButtonStateChanged.postValue(true)
        }else{
                whenDataLoaded.value?.payment?.forEach {
                    it.isSelected = false
                }

                whenDataLoaded.value?.payment?.getOrNull(position)?.apply {
                    isSelected = true
                }


            whenSubmitButtonStateChanged.postValue(true)
        }

    }

    fun submit(type: String , callPayment : String , channel : String) {
        var entity : PaymentEntity? = null
        var paymentC : Model.PaymentMethodModel? = null
        if(callPayment == "MBAPI" || (callPayment != "MBAPI" && channel == "MBANK")){
                if(channel == "BANK"){
                    paymentC = whenDataLoaded.value?.payment?.first { it.isSelected }
                }else if(channel == "MBANK"){
                    paymentC = whenDataLoaded.value?.mobilebanking?.first { it.isSelected }
                }else if(channel == "WALLET"){
                    paymentC = whenDataLoaded.value?.wallet?.first { it.isSelected }
                }
                val totalPrice = whenDataLoaded.value?.totalPrice?.toNumberFormatWithoutComma() ?: ""

                 entity = PaymentEntity().apply {
                    amount = totalPrice
                    paymentMethodId = paymentC?.paymentId ?: ""
                    paymentMethodName = paymentC?.paymentBankName ?: ""
                    description = type
                    paymentURL =  paymentC?.bankUrl?:""
                    callbankDesc = paymentC?.callBankDesc?:""
                }


            if (entity != null) {
                paymentManager.savePaymentMethod(entity)
            }

            whenIBankIsOpening.postValue(entity!!.paymentMethodName)

        } else{
                if(channel == "QR"){
                    val payment = whenDataLoaded.value?.qrChannel?.first { it.isSelected }
                    val totalPrice = whenDataLoaded.value?.totalPrice?.toNumberFormatWithoutComma() ?: ""
                    entity = PaymentEntity().apply {
                        amount = totalPrice
                        paymentMethodId = payment?.paymentId ?: ""
                        paymentMethodName = payment?.paymentBankName ?: ""
                        description = type
                        paymentURL =  payment?.bankUrl?:""
                        callbankDesc = payment?.callBankDesc?:""
                    }

                    paymentManager.savePaymentMethod(entity)

                    if (payment?.paymentId == BARCODE_ID) {
                        whenBarcodeIsOpening.postValue(true)
                        return
                    }

                    whenIBankIsOpening.postValue(entity.paymentMethodName)
                }else{
                    val payment = whenDataLoaded.value?.payment?.first { it.isSelected }
                    val totalPrice = whenDataLoaded.value?.totalPrice?.toNumberFormatWithoutComma() ?: ""
                    entity = PaymentEntity().apply {
                        amount = totalPrice
                        paymentMethodId = payment?.paymentId ?: ""
                        paymentMethodName = payment?.paymentBankName ?: ""
                        description = type
                        paymentURL =  payment?.bankUrl?:""
                        callbankDesc = payment?.callBankDesc?:""
                    }

                    paymentManager.savePaymentMethod(entity)

                    if (payment?.paymentId == BARCODE_ID) {
                        whenBarcodeIsOpening.postValue(true)
                        return
                    }

                    whenIBankIsOpening.postValue(entity.paymentMethodName)
                }
        }


    }

    fun checkStatusFromBankBySequenceId() {
//        whenLoading.postValue(true)


    }

    data class Model(
            val date: String,
            val licenseCarNo: String,
            val contractNumber: String,
            val fileList: List<FileDocumentModel>,
            val listInformationList: List<ListInformationModel>,
            val totalPrice: String,
            val payment: List<PaymentMethodModel>,
            val wallet: List<PaymentMethodModel>,
            val mobilebanking: List<PaymentMethodModel>,
            val qrChannel: List<PaymentMethodModel>,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {

        data class FileDocumentModel(val imageUrl: String)
        data class ListInformationModel(val title: String, val price: String)
        data class PaymentMethodModel(
                val paymentId: String = "",
                val imageUrl: String = "",
                val paymentBankName: String = "",
                val bankUrl: String = "",
                val callBankDesc: String = "",
                val channel: String = "",
                var isClicked: String = "",
                var isSelected: Boolean = false
        )
    }
}