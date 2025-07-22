package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.reprint.core.Reprint
import kotlinx.coroutines.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiCallback
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.*
import tlt.th.co.toyotaleasing.model.response.CheckApplicationStatusResponse
import tlt.th.co.toyotaleasing.model.response.GetAnnoucementResponse
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthPincodeViewModel : ViewModel() {

    private val userManager = UserManager.getInstance()

    val pincodeInvalidMessage = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect)
    val pincode3TimesInvalidMessage = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect_3_times)
    val pincode4TimesInvalidMessage = ContextManager.getInstance().getStringByRes(R.string.pincode_incorrect_4_times)
    val authAttemptOverLimitMessage = ContextManager.getInstance().getStringByRes(R.string.auth_attempt_over_limit_dialog_description)

    val whenLoading = MutableLiveData<Boolean>()
    val whenAuthSuccess = MutableLiveData<String>()
    val whenAuthFailure = MutableLiveData<String>()
    val whenAuthAttemptOverLimit = MutableLiveData<Boolean>()
    val whenShowFingerprintAuth = MutableLiveData<Boolean>()
    val whenCheckApplicationSuccess = MutableLiveData<Model>()
    val whenCheckApplicationFail = MutableLiveData<String>()
    val whenCallAnnounceSuccess = MutableLiveData<List<AnnounceModel>>()
    val whenCallAnnounceEmpty = MutableLiveData<String>()

    fun checkFingerprintSetting() {
        if (!Reprint.isHardwarePresent()
                || !Reprint.hasFingerprintRegistered()
                || !userManager.getProfile().isEnableFingerprintAuth) {
            return
        }

        whenShowFingerprintAuth.postValue(true)
    }

    fun checkLoginAttemptOverLimit() {
        if (!userManager.isLoginAttemptOverLimit()) {
            return
        }

        whenAuthFailure.postValue(authAttemptOverLimitMessage)
        whenAuthAttemptOverLimit.postValue(true)
    }

    fun loginByPincode(pincode: String) {
        userManager.resetLoginAttempt()
        whenAuthSuccess.postValue(pincode)
    }

    fun loginByFingerprint() {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)
            val currentLocation = LocationManager.getLastKnowLocation()
            val request = LoginByCustomerRequest.buildForFingerprint(
                    latitude = currentLocation.latitude.toString(),
                    longitude = currentLocation.longitude.toString())


            TLTApiManager.getInstance()
                    .loginByCustomer(request) { isError, result ->
                        whenLoading.postValue(false)

                        when (isError) {
                            true -> whenAuthFailure.postValue(result)
                            else -> whenAuthSuccess.postValue(result)
                        }
                    }
        }
    }

    fun checkApplicationStatus() {
        whenLoading.postValue(true)
        TLTApiManager.getInstance()
                .checkApplicationStatus(CheckApplicationStatusRequest.build()) { isError, result ->
                    whenLoading.postValue(false)

                    if (isError) {
                        whenCheckApplicationFail.postValue("")
                        return@checkApplicationStatus
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<CheckApplicationStatusResponse>::class.java)

                    val item = items.first()

                    val data = Model(
                            isShowForceUpdatePopup = item.flagAppStatus.toLowerCase() == "n",
                            forceUpdateMessage = item.statusDes,
                            isShowNoticePopup =  item.noticeStatus.toLowerCase() == "y",
                            NoticeMessage = item.noticeDes
                    )

                    val mockData = Model(
                            isShowForceUpdatePopup = false,
                            forceUpdateMessage = "",
                            isShowNoticePopup = true,
                            NoticeMessage = "TEST MESSAGE"
                    )
                    updateToken()
                    whenCheckApplicationSuccess.postValue(data)
                }
    }

    // --------- Add by Siri --------- //

    fun GetAnnouncement() {
        whenLoading.postValue(true)
        TLTApiManager.getInstance()
                .getAnnoucement(GetAnnoucementRequest.build()) { isError, result ->
                    whenLoading.postValue(false)

                    if (isError) {
                        whenCheckApplicationFail.postValue("")
                        return@getAnnoucement
                    }


                    val data = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<GetAnnoucementResponse>::class.java)
                            .toList()
                            .map {
                                AnnounceModel(
                                        anmId = it.aNM_ID ?:  ""
                                        ,urlLink = it.uRL_LINK ?:  ""
                                        , message = it.mESSAGE ?:  ""
                                        , linkRef =  it.linkRef?:  ""
                                )
                            }
                    if(data.size > 0 ){
                        whenCallAnnounceSuccess.postValue(data)
                        CheckAnnouncement(data)
                    }else{
                        whenCallAnnounceEmpty.postValue("")
                        return@getAnnoucement
                    }

                }
    }


    fun CheckAnnouncement(data: List<AnnounceModel>) {
        GlobalScope.launch(Dispatchers.Main) {
            val requestList = withContext(Dispatchers.Default) {
                data.map {
                    CheckedAnnoucementRequest.build(
                            admID = it.anmId
                    )
                }
            }
            withContext(Dispatchers.Default) { upload(requestList) }
        }
    }


    private suspend fun upload(requests: List<CheckedAnnoucementRequest>) = suspendCoroutine<Boolean> { suspend ->
        GlobalScope.launch(Dispatchers.Main) {

            requests.map {
                GlobalScope.async { uploadToApi(it) }
            }.forEach {
                val isError = it.await()
                isError.ifTrue {
                    suspend.resume(false)
                    return@launch
                }
            }
            suspend.resume(true)
        }
    }

    private suspend fun uploadToApi(request: CheckedAnnoucementRequest) = suspendCoroutine<Boolean> {
        TLTApiManager.getInstance().checkedAnnoucement(request) { isError: Boolean, result: String ->
            it.resume(isError)
        }
    }

    data class AnnounceModel(
            val anmId: String = "",
            val urlLink: String = "",
            val message: String = "",
            val linkRef: String = ""
    )

    // -------- End by Siri ---------- //

    private suspend fun hashAuthPincode(pincode: String) = suspendCoroutine<String> {
        if (pincode.isEmpty()) {
            it.resume("")
            return@suspendCoroutine
        }

        val hash = UserManager.getInstance().hashPincodeForAuth(pincode)
        it.resume(hash)
    }


    fun updateToken() {
        try {
            val updateTokenRequest = UpdateTokenRequest.build()
            prepareData(updateTokenRequest)
        }catch (e : Exception){
            e.stackTrace.toString()
        }
    }

    private fun prepareData(request: UpdateTokenRequest) {

        TLTApiManager.getInstance().updateToken(request) { _, _ ->
        }
    }


    data class Model(
            val isShowForceUpdatePopup: Boolean = false,
            val forceUpdateMessage: String = "",
            val isShowNoticePopup: Boolean = false,
            val NoticeMessage: String = ""
    )
}