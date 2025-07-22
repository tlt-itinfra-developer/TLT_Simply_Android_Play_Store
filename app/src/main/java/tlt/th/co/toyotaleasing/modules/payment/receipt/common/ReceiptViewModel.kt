package tlt.th.co.toyotaleasing.modules.payment.receipt.common

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.ifFalse
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.toDatetimeReceipt
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.CheckStatusFromBankResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.util.CalendarUtils

class ReceiptViewModel : BaseViewModel() {

    private val paymentManager = PaymentManager
    private val currentCar = CacheManager.getCacheCar()

    val whenDataLoaded = MutableLiveData<Model>()

    fun getData(isDelay: Boolean, receiptData: CheckStatusFromBankResponse) {

        isDelay.ifFalse {

            val paymentMethod = paymentManager.getPaymentMethod()

            val data = Model(
                    currentDate = getCurrentDate(),
                    fullname = UserManager.getInstance().getProfile().name,
                    carLicense = "${currentCar?.regNumber} - ${currentCar?.cREGPROVINCE}",
                    contractNumber = currentCar?.contractNumber ?: "",
                    paymentMethod = paymentMethod?.paymentMethodName ?: "",
                    status = Status.PAID_SUCCESS
            )

            whenDataLoaded.postValue(data)
        }

        //MasterDataManager.getInstance().getBankNameByBankCode(receiptData.bankCode.toString()) [Get bank name by bank code]
//receiptData.currentDate!!.toDatetimeReceipt()

        isDelay.ifTrue {

            val data = Model(
                    currentDate = getCurrentDate(),
                    fullname = receiptData.customerName ?: "-",
                    carLicense = receiptData.cRegNo ?: "-",
                    contractNumber = receiptData.extContract ?: "-",
                    paymentMethod = receiptData.getBankName(),
                    totalPaid = receiptData.amount ?: "-",
                    receiptData = receiptData,
                    status = Status.PAID_SUCCESS
            )

            whenDataLoaded.postValue(data)
        }

    }

    private fun getCurrentDate(): String {
        val currentDate = CalendarUtils.getCurrentDateByPattern("dd/MM/yyyy HH:mm:ss")
        return currentDate.toDatetimeReceipt()
    }

    data class Model(
            val currentDate: String = "",
            val fullname: String = "",
            val carLicense: String = "",
            val contractNumber: String = "",
            val paymentMethod: String = "",
            val totalPaid: String = "",
            val status: Status,
            val receiptData: CheckStatusFromBankResponse = CheckStatusFromBankResponse(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val type: Type
            get() {
                return when (receiptData.type!!.toLowerCase()) {
                    "contract" -> Type.INSTALLMENT
                    "insurance" -> Type.INSURANCE
                    "tax" -> Type.TAX
                    else -> Type.OTHER
                }
            }
    }

    enum class Type {
        INSTALLMENT,
        INSURANCE,
        TAX,
        OTHER
    }

    enum class Status {
        PAID_FAIL,
        PAID_SUCCESS
    }
}