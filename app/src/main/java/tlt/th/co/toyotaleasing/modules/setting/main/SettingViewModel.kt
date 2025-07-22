package tlt.th.co.toyotaleasing.modules.setting.main

import androidx.lifecycle.MutableLiveData
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.exception.ApiException
import tlt.th.co.toyotaleasing.common.extension.toNumberWithoutDecimal
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.SetTouchIDRequest
import tlt.th.co.toyotaleasing.model.request.SettingApplicationRequest
import tlt.th.co.toyotaleasing.model.request.UploadImagesRequest
import tlt.th.co.toyotaleasing.model.response.GetDataProfileResponse
import tlt.th.co.toyotaleasing.util.AppUtils
import tlt.th.co.toyotaleasing.util.ImageUtils
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SettingViewModel : BaseViewModel() {

    val whenDataLoaded = MutableLiveData<Model>()

    private val apiManager = TLTApiManager.getInstance()
    private val userManager = UserManager.getInstance()

    fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                whenLoading.postValue(true)

                val dataProfileResponse = getDataProfile()
                val model = convertToModel(dataProfileResponse)

                whenDataLoaded.postValue(model)
            } catch (e: ApiException) {
                val dataProfileResponse = GetDataProfileResponse()
                val model = convertToModel(dataProfileResponse)
                whenDataLoaded.postValue(model)
                whenDataFailure.postValue(e.message)
            } finally {
                whenLoading.postValue(false)
            }
        }
    }

    fun updateProfileImage(imageUri: Uri? = Uri.EMPTY) {
        if (imageUri == null || imageUri == Uri.EMPTY) {
            return
        }

        GlobalScope.launch(Dispatchers.Main) {
            val base64 = ImageUtils.encodeToBase64(imageUri)
            val request = UploadImagesRequest.buildForImageProfile(base64)
            apiManager.uploadImages(request) { _, _ -> }
        }
    }

    fun updateProfile(installmentConfig: String = "7",
                      taxConfig: String = "30",
                      insuranceConfig: String = "60",
                      isNewsEnabled: Boolean = false,
                      isTIBEnabled: Boolean = false,
                      language: String = "TH",
                      isFingerprintEnable: Boolean = false) {
        val settingApplicationRequest = SettingApplicationRequest.build(
                lang = language,
                installment = installmentConfig,
                tax = taxConfig,
                insurance = insuranceConfig,
                flagNews = if (isNewsEnabled) "Y" else "N",
                flagTIB = if (isTIBEnabled) "Y" else "N"
        )

        val setTouchIDRequest = if (isFingerprintEnable) {
            userManager.enableFingerprintAuth()
            SetTouchIDRequest.buildForActivate()
        } else {
            userManager.disableFingerprintAuth()
            SetTouchIDRequest.buildForDeactivate()
        }

        apiManager.settingApplication(settingApplicationRequest) { _, _ -> }
        apiManager.setTouchID(setTouchIDRequest) { _, _ -> }
    }

    private fun convertToModel(profile: GetDataProfileResponse): Model {
        val status = if (userManager.isCustomer()) {
            Status.CUSTOMER
        } else {
            Status.NON_CUSTOMER
        }

        return Model(
                name = profile.cUSTNAME ?: "",
                imageUrl = profile.pROIMAGE ?: "",
                defaultImageUrl = "",
                email = profile.eMAIL ?: "",
                emailStatus = profile.flagNotVerEmail ?: "",
                notiInstallment = profile.iNSTALLMENTCONFIG?.toNumberWithoutDecimal() ?: "",
                notiTax = profile.tAXCONFIG?.toNumberWithoutDecimal() ?: "",
                notiInsurance = profile.iNSURANCECONFIG?.toNumberWithoutDecimal() ?: "",
                isEnabledNewsAndPromotion = profile.fLAGNEWS!!.toLowerCase() == "y" || profile.fLAGNEWS!!.isEmpty(),
                isEnabledTIBClubs = profile.fLAGTIBCLUB!!.toLowerCase() == "y" || profile.fLAGTIBCLUB!!.isEmpty(),
                language = profile.lANGUAGE ?: "",
                isEnabledIDTouch = userManager.isFingerprintEnabled(),
                status = status
        )
    }

    private suspend fun getDataProfile() = suspendCoroutine<GetDataProfileResponse> {
        apiManager.getDataProfile { isError, result ->
            if (isError) {
                it.resumeWithException(ApiException(result))
                return@getDataProfile
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetDataProfileResponse>::class.java)

            if (items.isEmpty()) {
                it.resumeWithException(ApiException(result))
                return@getDataProfile
            }

            val item = items.first()

            CacheManager.cacheProfile(item)

            it.resume(item)
        }
    }

    fun changeToResetPincodeFlow() {
        FlowManager.changeToResetPinFlow()
    }

    data class Model(
            val name: String = "",
            val imageUrl: String = "",
            val defaultImageUrl: String = "",
            val email: String = "",
            val emailStatus: String = "",
            val notiInstallment: String = "",
            val notiTax: String = "",
            val notiInsurance: String = "",
            val isEnabledNewsAndPromotion: Boolean = false,
            val isEnabledTIBClubs: Boolean = false,
            val language: String = "",
            val isEnabledIDTouch: Boolean = false,
            val status: Status = Status.NON_CUSTOMER,
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    ) {
        //จริง ๆ ต้องเป็น Y ตอนนี้เปลี่ยนเป็น N เพื่อทดสอบก่อน เนื่องจากเค้าส่งมาให้แค่ N
        val isNotVerifyEmail: EmailStatus = if (emailStatus.toLowerCase() == "y") {
            EmailStatus.PENDING
        } else {
            EmailStatus.VERIFIED
        }
    }

    enum class Status {
        CUSTOMER,
        NON_CUSTOMER
    }

    enum class EmailStatus {
        PENDING,
        VERIFIED
    }
}