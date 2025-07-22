package tlt.th.co.toyotaleasing.modules.qrcode.detail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.extension.toNumberFormatWithoutComma
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.PaymentManager
import tlt.th.co.toyotaleasing.modules.payment.checkout.common.CheckoutViewModel
import tlt.th.co.toyotaleasing.util.AppUtils

class QRViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    private val currentCar = CacheManager.getCacheCar()
    private val profile = CacheManager.getCacheProfile()
    lateinit var transactionType: QRActivity.TransactionType

    fun getData() {
    }

    private fun getQRcode(): String {
        return "|${currentCar?.aCCOUNTNO}\n${currentCar?.rEFCODE1}\n${getRefCode2()}\n0"
    }

    private fun getBarcode(): String {
        return "|${currentCar?.aCCOUNTNO}\n${currentCar?.rEFCODE1}\n${getRefCode2()}\n0"
    }

    private fun getBarcodeDisplay(): String {
        return "${currentCar?.aCCOUNTNO} ${currentCar?.rEFCODE1} ${getRefCode2()} 0"
    }

    private fun getRefCode2(): String {
        return when (transactionType) {
            QRActivity.TransactionType.INSTALLMENT -> currentCar?.cREFCODE2 ?: ""
            QRActivity.TransactionType.TAX -> currentCar?.tREFCODE2 ?: ""
            QRActivity.TransactionType.INSURANCE -> currentCar?.iREFCODE2 ?: ""
            else -> currentCar?.pREFCODE2 ?: ""
        }
    }

    data class Model(
            val qrcode: String = "",
            val barcode: String = "",
            val barcodeDisplay: String = "",
            val fullName: String = "",
            val carLicense: String = "",
            val contractNumber: String = "",
            val referenceNo1: String = "",
            val referenceNo2: String = "",
            val totalPrice: String = "",
            val fileList: MutableList<CheckoutViewModel.Model.ListInformationModel>,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}