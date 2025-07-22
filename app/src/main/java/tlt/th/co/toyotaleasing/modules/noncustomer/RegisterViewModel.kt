package tlt.th.co.toyotaleasing.modules.noncustomer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import tlt.th.co.toyotaleasing.common.extension.ifTrue
import tlt.th.co.toyotaleasing.common.extension.isEmail
import tlt.th.co.toyotaleasing.common.livedata.SingleLiveData
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.manager.social.SocialProfile
import tlt.th.co.toyotaleasing.model.entity.RegisterEntity
import tlt.th.co.toyotaleasing.model.request.*
import tlt.th.co.toyotaleasing.model.response.CheckApplicationStatusResponse
import tlt.th.co.toyotaleasing.model.response.GetAnnoucementResponse
import tlt.th.co.toyotaleasing.model.response.ItemBannerResponse
import tlt.th.co.toyotaleasing.view.banner.Banner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RegisterViewModel : ViewModel() {

    val whenRegisterByEmail = SingleLiveData<Boolean>()
    val whenRegisterBySocial = SingleLiveData<Boolean>()
    val whenGetDefaultEmail = SingleLiveData<String>()
    val whenGetBannerList = SingleLiveData<ArrayList<Banner>>()
    val whenLoading = SingleLiveData<Boolean>()
    val whenEmailInvalid = SingleLiveData<Boolean>()
    val whenCheckApplicationSuccess = MutableLiveData<Model>()
    val whenCheckApplicationFail = MutableLiveData<String>()
    val whenCallAnnounceSuccess = MutableLiveData<List<AnnounceModel>>()


    fun getDefaultEmail() {
        val email = ""

        whenGetDefaultEmail.value = email
    }

    fun getBannerListFromApi() {
        whenLoading.value = true

        TLTApiManager.getInstance()
                .getBanners(BannerRequest.build()) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        whenGetBannerList.value = ArrayList()
                        return@getBanners
                    }

                    val banners = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemBannerResponse>::class.java)
                            .toList()
                            .filter {
                                it.bannertype.toLowerCase() == "banner"
                            }
                            .map { Banner(it.bannerindex, it.bannerimg, it.bannername, it.bannerdesC1, it.bannerdesC2) }

                    whenGetBannerList.value = ArrayList(banners)
                }
    }

    fun whenEmailPressed(email: String) {
        if (!email.isEmail()) {
            whenEmailInvalid.value = true
            return
        }

        val sendEmailRegisterRequest = SendEmailRegisterRequest.build(email)

        whenLoading.value = true

        TLTApiManager.getInstance()
                .custRegisLoad { _, _ ->
                    TLTApiManager.getInstance()
                            .sendEmailRegister(sendEmailRegisterRequest) { isError, result ->
                                val registerState = RegisterEntity().apply {
                                    this.email = email
                                }


                                FlowManager.changeToRegisterFlow()

                                whenLoading.value = false
                                whenRegisterByEmail.value = isError
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
                            isShowNoticePopup = item.noticeStatus.toLowerCase() == "y",
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




    fun whenSocialPressed(socialProfile: SocialProfile) {
        whenLoading.value = true

        val request = SocialRegisterRequest.buildBySocialProfile(socialProfile)

        TLTApiManager.getInstance()
                .custRegisLoad { isError, result ->
                    if (isError) {
                        whenLoading.value = false
                        whenRegisterBySocial.value = isError
                        return@custRegisLoad
                    }

                    TLTApiManager.getInstance().socialRegister(request) { isError, result ->
                        if (!isError) {


                            //DatabaseManager.getInstance().saveRegisterData(registerState)
                        }

                        FlowManager.changeToRegisterFlow()

                        whenLoading.value = false
                        whenRegisterBySocial.value = isError
                    }
                }
    }

    data class Model(
            val isShowForceUpdatePopup: Boolean = false,
            val forceUpdateMessage: String = "",
            val isShowNoticePopup: Boolean = false,
            val NoticeMessage: String = ""
    )

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
                                        , urlLink = it.uRL_LINK ?:  ""
                                        , message = it.mESSAGE ?:  ""
                                        , linkRef =  it.linkRef?:  ""
                                )
                            }
                    whenCallAnnounceSuccess.postValue(data)
                    CheckAnnouncement(data)
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


    data class AnnounceModel(
            val anmId: String = "",
            val urlLink: String = "",
            val message: String = "",
            val linkRef: String = ""
    )

    // -------- End by Siri ---------- //
}