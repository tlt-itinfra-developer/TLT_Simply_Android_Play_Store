package tlt.th.co.toyotaleasing.modules.installmentdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.GetDataInstallmentRequest
import tlt.th.co.toyotaleasing.model.request.GetInstallmentDetailRequest
import tlt.th.co.toyotaleasing.model.response.ItemInstallmentDetailResponse
import tlt.th.co.toyotaleasing.model.response.ItemInstallmentResponse

class InstallmentDetailViewModel : ViewModel() {

    private val apiManager = TLTApiManager.getInstance()
    private val localizeManager = LocalizeManager
    private val jsonMapperManager = JsonMapperManager.getInstance()

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<InstallmentDetailViewModel.Model>()
    val whenDisplayUIByStatus = MutableLiveData<Status>()

    fun getInstallmentDetail() {
        whenLoading.value = true

        val contractNumber = CacheManager.getCacheCar()?.contractNumber ?: ""
        val installmentRequest = GetDataInstallmentRequest.build(contractNumber)
        val installmentDetailRequest = GetInstallmentDetailRequest.build(contractNumber)

        apiManager.getDataInstallment(installmentRequest) { isError: Boolean, result: String ->
            if (isError) {
                whenLoading.value = false
                return@getDataInstallment
            }

            val items = jsonMapperManager.gson.fromJson(result, Array<ItemInstallmentResponse>::class.java)

            if (items.isEmpty()) {
                whenLoading.value = false
                return@getDataInstallment
            }

            val installment = items.first()

            apiManager.getInstallmentDetail(installmentDetailRequest) { isError: Boolean, result: String ->
                whenLoading.value = false

                if (isError) {
                    return@getInstallmentDetail
                }

                val debtList = jsonMapperManager
                        .gson.fromJson(result, Array<ItemInstallmentDetailResponse>::class.java)
                        .map {
                            val datadesc = if (localizeManager.getLanguage() == LocalizeManager.ENGLISH) {
                                it.dATADESCEN
                            } else {
                                it.dATADESC
                            }

                            Model.Debt(datadesc, it.dATASUMTOTAL.toNumberFormat())
                        }

                val data = Model(
                        date = installment.cURRENTDATE.toDatetime(),
                        carLicense = "${installment.cREGNO} ${installment.cREGPROVINCE}",
                        fullname = installment.cUSTNAME,
                        carNo = installment.eXTCONTRACT,
                        payment =  installment.cINSTALLAMT.toNumberFormat(),
                        debtList =  debtList,
                        paymentMethod = installment.paymentMethod,
                        accountName = installment.bankAccName,
                        accountBank = installment.bankName,
                        accountNumber = installment.bankAccNo,
                        nextPayment = installment.cNEXTDUEDATE.toDatetime(),
                        totalPayment = installment.cSUMAMT.toNumberFormat(),
                        followupfee = installment.followupfee,
                        _status =  installment.contractDescription
                )


                whenDataLoaded.value = data
                whenDisplayUIByStatus.value = data.status
            }
        }
    }

    private fun observeMockData() {
        whenLoading.value = false

        val data = Model(
                date = "11 ม.ค. 2561" ,
                carLicense = "กต - 1528 กรุงเทพมหานคร",
                fullname = "นายอนุพล รักจริง",
                carNo = "APH1789534",
                payment = "10,608.00",
                debtList =  listOf(
                        Model.Debt("ค่างวดที่ค้างชำระเดือน ม.ค.", "10,608.00"),
                        Model.Debt("ค่าเบี้ยปรับล่าช้าสะสม", "1,781.00")
                ),
                paymentMethod = "Direct Debit",
                accountName ="นายอนุพล รักจริง",
                accountBank ="ธนาคารกรุงเทพ",
                accountNumber = "0123456789",
                nextPayment = "วันที่ชำระงวดต่อไป 15 มีค. 2561",
                totalPayment = "ยอดชำระ 1,781.00 บาท",
                followupfee = "" ,
                _status = "10"

        )



        whenDataLoaded.value = data
        whenDisplayUIByStatus.value = data.status
    }

    data class Model(
            val date: String = "",
            val carLicense: String = "",
            val fullname: String = "",
            val carNo: String = "",
            val payment: String = "",
            val debtList: List<Debt>,
            val paymentMethod: String = "",
            val accountName: String = "",
            val accountBank: String = "",
            val accountNumber: String = "",
            val nextPayment: String = "",
            val totalPayment: String = "",
            val followupfee : String = "",
            private val _status: String = ""
    ) {
        val status: Status
            get() {
//                val fixStatus = _status.toFloat().toInt().toString()
//
//                val normalStatusList = arrayOf("10")
//                val debtStatusList = arrayOf("10", "21", "22", "40")
//                val closedStatusList = arrayOf("81", "91", "92")
//                val legalStatusList = arrayOf("32", "69", "71", "72", "74", "76", "77", "79", "86", "88", "96", "99")
//
//                return when {
//                    normalStatusList.contains(fixStatus) -> Status.NORMAL
//                    debtStatusList.contains(fixStatus) -> Status.DEBT
//                    closedStatusList.contains(fixStatus) -> Status.CLOSED
//                    legalStatusList.contains(fixStatus) -> Status.LEGAL
//                    else -> Status.OTHER
//                }

                return when (_status.toLowerCase()) {
                    "close contract" -> Status.CLOSED
                    "legal contract" -> Status.LEGAL
                    "other contract" -> Status.OTHER
                    else -> if (paymentMethod == "DirectDebit") {
                        Status.DIRECT_DEBIT
                    } else {
                        Status.NORMAL
                    }
                }
            }

        data class Debt(
                val title: String = "",
                val price: String = ""
        )
    }

    enum class Status {
        NORMAL,
        DIRECT_DEBIT,
        CLOSED,
        LEGAL,
        OTHER
    }
}