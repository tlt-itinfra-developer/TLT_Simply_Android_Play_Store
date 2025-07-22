package tlt.th.co.toyotaleasing.modules.insurance

import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.toDatetime
import tlt.th.co.toyotaleasing.common.extension.toNumberFormat
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.BannerRequest
import tlt.th.co.toyotaleasing.model.request.GetDataInsuranceRequest
import tlt.th.co.toyotaleasing.model.response.GetDataInsuranceResponse
import tlt.th.co.toyotaleasing.model.response.ItemBannerResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.view.banner.Banner

class InsuranceViewModel : BaseViewModel() {

    private val userManager = UserManager.getInstance()
    private val apiManager = TLTApiManager.getInstance()
    private val tibClub = null

    val whenDataLoaded = SingleLiveData<Model>()
    val whenBannerLoaded = SingleLiveData<List<Banner>>()

    fun getData(contractNumber: String = "") {
        getBanners()

        if (!userManager.isCustomer()) {
            getDataForNonCustomer()
            return
        }

        val extContract = if (contractNumber.isNotEmpty()) {
            contractNumber
        } else {
            CacheManager.getCacheCar()?.contractNumber ?: ""
        }

        getDataForCustomer(extContract)
    }

    private fun getBanners() {
        apiManager.getBanners(BannerRequest.build()) { isError, result ->
            if (isError) {
                return@getBanners
            }

            val banners = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<ItemBannerResponse>::class.java)
                    .toList()
                    .filter {
                        it.bannertype.toLowerCase() == "tibbanner"
                    }
                    .map { Banner(it.bannerindex, it.bannerimg, it.bannername, it.bannerdesC1, it.bannerdesC2) }

            whenBannerLoaded.postValue(banners)
        }
    }

    private fun getDataForNonCustomer() {
        val model = Model(
                "",
                "",
                "",
                "",
                "",
                ""
        )

        whenDataLoaded.postValue(model)
    }

    private fun getDataForCustomer(extContract: String) {
        whenLoading.postValue(true)

        val request = GetDataInsuranceRequest.build(extContract)

        if (AppUtils.isLocalhostEnvironment()) {
            apiManager.getDataInsuranceLocalhost(request) { isError: Boolean, result: String ->
                whenLoading.postValue(false)

                if (isError) {
                    return@getDataInsuranceLocalhost
                }

                val insurances = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<GetDataInsuranceResponse>::class.java)

                if (insurances.isEmpty()) {
                    return@getDataInsuranceLocalhost
                }

                val insurance = insurances.first()
                CacheManager.cacheInsurance(insurance)

                val model = Model(
                        insurance.cURRENTDATE?.toDatetime() ?: "",
                        "${insurance.cREGNO} - ${insurance.cREGPROVINCE}",
                        insurance.cUSTNAME ?: "",
                        insurance.eXTCONTRACT ?: "",
                        insurance.iSUMAMT?.toNumberFormat() ?: "",
                        ""
                )

                whenDataLoaded.postValue(model)
            }
        } else {

            apiManager.getDataInsurance(request) { isError: Boolean, result: String ->
                whenLoading.postValue(false)

                if (isError) {
                    return@getDataInsurance
                }

                val insurances = JsonMapperManager.getInstance()
                        .gson.fromJson(result, Array<GetDataInsuranceResponse>::class.java)

                if (insurances.isEmpty()) {
                    return@getDataInsurance
                }

                val insurance = insurances.first()
                CacheManager.cacheInsurance(insurance)

                val model = Model(
                        insurance.cURRENTDATE?.toDatetime() ?: "",
                        "${insurance.cREGNO} - ${insurance.cREGPROVINCE}",
                        insurance.cUSTNAME ?: "",
                        insurance.eXTCONTRACT ?: "",
                        insurance.iSUMAMT?.toNumberFormat() ?: "",
                        ""
                )

                whenDataLoaded.postValue(model)
            }
        }
    }

    data class Model(
            val currentDate: String = "",
            val license: String = "",
            val fullname: String = "",
            val contractNumber: String = "",
            val totalPay: String = "",
            val tibClubImageUrl: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        val status: Status
            get() {
                val insurance = CacheManager.getCacheInsurance()

                return if (!UserManager.getInstance().isCustomer()) {
                    Status.NON_CUSTOMER
                } else if (insurance?.flagPROHIBIT!!.toLowerCase() == "y") {
                    Status.PROHIBIT
                } else if (insurance.flag4M!!.toLowerCase() == "e") {
                    Status.NO_DATA
                } else if (insurance.flag4M!!.toLowerCase() == "y") {
                    when {
                        insurance.flagINSPROCESS!!.toLowerCase() == "y" -> Status.CHECKING
                        insurance.flagSETTLEMENT!!.toLowerCase() == "y" -> Status.AFTER_PAY
                        else -> Status.NORMAL
                    }
                } else {
                    Status.NO_DATA
                }
            }
    }

    enum class Status {
        NO_DATA,
        NON_CUSTOMER,
        NORMAL,
        CHECKING,
        AFTER_PAY,
        PROHIBIT
    }
}