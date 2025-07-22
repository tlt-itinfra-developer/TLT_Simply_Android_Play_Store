package tlt.th.co.toyotaleasing.modules.insurance.quotation

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager

import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.entity.masterdata.MailingA
import tlt.th.co.toyotaleasing.model.request.RequestQuotationRequest
import tlt.th.co.toyotaleasing.util.AppUtils

class QuotationViewModel : BaseViewModel() {

    val whenDefaultDataLoaded = MutableLiveData<Model>()
    val whenDisplayByStatus = MutableLiveData<Status>()
    val whenSendQuotationFailed = MutableLiveData<String>()
    val whenSendQuotationSuccess = MutableLiveData<Boolean>()

    private val context = ContextManager.getInstance().getApplicationContext()
    private val masterDataManager = MasterDataManager.getInstance()
    private val isThaiLanguage = LocalizeManager.isThai()
    private val apiManager by lazy { TLTApiManager.getInstance() }
    private val insurance = CacheManager.getCacheInsurance()
    private val myProfile = CacheManager.getCacheProfile()
    private val currentCar = CacheManager.getCacheCar()

    fun getData() {
        val model = if (isCustomer()) {
            getCustomerData()
        } else {
            getNonCustomerData()
        }

        whenDefaultDataLoaded.postValue(model)
        whenDisplayByStatus.postValue(model.status)
    }

    fun requestQuotation(customerName: String = "",
                         customerPhone: String = "",
                         customerEmail: String = "",
                         carLicense: String = "",
                         provinceIndex: Int = 0,
                         vehicleModel: String = "",
                         requestQuotationList: ArrayList<QuotationModel> = arrayListOf(),
                         remark: String = "",
                         isStaffContact: Boolean = true) {
        whenLoading.postValue(true)

        val quotationList = requestQuotationList.mapIndexed { index, quotationModel ->
            val insuranceCompany = false
            val insurancePolicy = false
            val paymentMethod = false

        }

        val request = if (isCustomer()) {
            buildForCustomer(
                    customerPhone,
                    customerEmail,
                    customerName,
                    remark,
                    vehicleModel,
                    listOf(),
                    isStaffContact
            )
        } else {
            val province = masterDataManager.getProvinceList()?.get(provinceIndex)!!

            buildForNonCustomer(
                    province,
                    customerPhone,
                    customerEmail,
                    customerName,
                    carLicense,
                    vehicleModel,
                    remark,
                    listOf(),
                    isStaffContact
            )
        }

        apiManager.requestQuotation(request) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                whenSendQuotationFailed.postValue(result)
                return@requestQuotation
            }

            whenSendQuotationSuccess.postValue(true)
        }
    }

    private fun getProvinces(): List<String> {
        return masterDataManager.getProvinceList()?.map {
            if (isThaiLanguage) {
                it.pROVINCENAMETH!!
            } else {
                it.pROVINCENAMEEN!!
            }
        } ?: listOf()
    }

    private fun getInsuranceCompanies(): MutableList<String> {
        return mutableListOf()
    }

    private fun getInsurancePolicies(): MutableList<String> {
        return mutableListOf()
    }

    private fun getPaymentMethods(): MutableList<String> {
        return mutableListOf()
    }

    private fun getCustomerData(): Model {
        val provinceList = getProvinces()
        val insuranceCompanyList = getInsuranceCompanies()
        val insurancePolicyList = getInsurancePolicies()
        val paymentMethodList = getPaymentMethods()

        insuranceCompanyList.add(0, context.getString(R.string.quotation_insurance_company_hint))
        insurancePolicyList.add(0, context.getString(R.string.quotation_insurance_policy_hint))
        paymentMethodList.add(0, context.getString(R.string.quotation_insurance_payment_method_hint))

        return Model(
                insurance?.cURRENTDATE?.toDatetime() ?: "",
                insurance?.cUSTNAME ?: "",
                insurance?.cREGNO ?: "",
                insurance?.eXTCONTRACT ?: "",
                getCarModel(insurance?.eXTCONTRACT ?: ""),
                myProfile?.pHONE ?: "",
                myProfile?.eMAIL ?: "",
                provinceList,
                insuranceCompanyList,
                insurancePolicyList,
                paymentMethodList
        )
    }

    private fun getCarModel(extContract: String): String {
        return ""
    }

    private fun getNonCustomerData(): Model {
        val provinceList = getProvinces()
        val insuranceCompanyList = getInsuranceCompanies()
        val insurancePolicyList = getInsurancePolicies()
        val paymentMethodList = getPaymentMethods()

        insuranceCompanyList.add(0, context.getString(R.string.quotation_insurance_company_hint))
        insurancePolicyList.add(0, context.getString(R.string.quotation_insurance_policy_hint))
        paymentMethodList.add(0, context.getString(R.string.quotation_insurance_payment_method_hint))

        return Model(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                provinceList,
                insuranceCompanyList,
                insurancePolicyList,
                paymentMethodList
        )
    }

    private fun buildForCustomer(customerPhone: String = "",
                                 customerEmail: String = "",
                                 customerName: String = "",
                                 remark: String = "",
                                 vehicleModel: String = "",
                                 quotationList: List<RequestQuotationRequest.PKEY12> = listOf(),
                                 isStaffContact: Boolean = true): RequestQuotationRequest {
        val currentCar = CacheManager.getCacheCar()

        return RequestQuotationRequest.build(
                currentCar?.contractNumber ?: "",
                customerName,
                currentCar?.regNumber ?: "",
                currentCar?.cREGPROVINCE ?: "",
                "",
                vehicleModel,
                customerPhone,
                customerEmail,
                remark,
                isStaffContact,
                quotationList
        )
    }

    private fun buildForNonCustomer(province: MailingA,
                                    customerPhone: String = "",
                                    customerEmail: String = "",
                                    customerName: String = "",
                                    regisNo: String = "",
                                    carModel: String = "",
                                    remark: String = "",
                                    quotationList: List<RequestQuotationRequest.PKEY12> = listOf(),
                                    isStaffContact: Boolean = true): RequestQuotationRequest {
        return RequestQuotationRequest.build(
                "",
                customerName,
                regisNo,
                province.pROVINCENAMETH!!,
                province.pROVINCECODE!!,
                carModel,
                customerPhone,
                customerEmail,
                remark,
                isStaffContact,
                quotationList
        )
    }

    fun isCustomer() = UserManager.getInstance().isCustomer()

    data class Model(
            val date: String = "",
            val fullname: String = "",
            val carLicense: String = "",
            val contractNumber: String = "",
            val vehicalModel: String = "",
            val phoneNumber: String = "",
            val email: String = "",
            val provinceList: List<String> = listOf(),
            val insuranceCompanyList: List<String> = listOf(),
            val insurancePolicyList: List<String> = listOf(),
            val paymentMethodList: List<String> = listOf(),
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() {
                return if (UserManager.getInstance().isCustomer()) {
                    Status.CUSTOMER
                } else {
                    Status.NON_CUSTOMER
                }
            }
    }

    enum class Status {
        NON_CUSTOMER,
        CUSTOMER
    }
}