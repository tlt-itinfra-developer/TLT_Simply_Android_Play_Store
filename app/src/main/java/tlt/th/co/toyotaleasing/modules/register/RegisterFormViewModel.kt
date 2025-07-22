package tlt.th.co.toyotaleasing.modules.register

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.isDoesNotContainCharacter
import tlt.th.co.toyotaleasing.common.extension.isEmail
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.BeforeRegisterRequest
import tlt.th.co.toyotaleasing.model.request.GetPhonenumberRequest
import tlt.th.co.toyotaleasing.model.response.ItemPhoneNumberResponse
import tlt.th.co.toyotaleasing.modules.selectphone.PhoneNumber
import tlt.th.co.toyotaleasing.util.AppUtils

class RegisterFormViewModel : BaseViewModel() {

    val whenGetDefaultData = MutableLiveData<Model>()
    val whenEmailInvalid = MutableLiveData<Boolean>()
    val whenIdCardInvalid = MutableLiveData<Boolean>()
    val whenContractInvalid = MutableLiveData<Boolean>()
    val whenSimplyCardInvalid = MutableLiveData<Boolean>()
    val whenPrepareDataLoaded = MutableLiveData<Boolean>()
    val whenPhoneNumberListLoaded = MutableLiveData<ArrayList<PhoneNumber>>()
    val whenGetPhoneNumberError = MutableLiveData<Boolean>()

    //8032744408161 6755457820214 7388773364147 6054068300355 6166335812043 9753045892318
    //99000500001(7388) 99000500002(6166)

    fun getDefaultData() {
        val email = ""
        val idCard = if (AppUtils.isDebug() && BuildConfig.AUTO_FILL) "99999100004" else ""

        val data = Model(
                email = email,
                idCard = idCard
        )

        whenGetDefaultData.value = data
    }

    fun register(email: String, simplyId: String) {

        val beforeRegisterRequest = BeforeRegisterRequest.build(simplyId)
        prepareData(beforeRegisterRequest)
    }

    private fun formValidate(email: String, simplyId: String, flow: String): Boolean {
        var valid = true
        if (!email.isEmail()) {
            whenEmailInvalid.postValue(true)
            valid = false
        }

        if (simplyId.isNullOrEmpty()) {
            whenIdCardInvalid.postValue(true)
            valid = false
        }

        // By23
//              if (!simplyId.isDoesNotContainCharacter() && flow != RegisterFormActivity.TYPE_CONTRACT
//                && flow != RegisterFormActivity.TYPE_PASSPORT) {
//            whenIdCardInvalid.postValue(true)
//            valid = false
//        }

        // by siri 25/10/2019
        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty() && flow == RegisterFormActivity.TYPE_SIMPLY ){
            whenSimplyCardInvalid.postValue(true)
            valid = false
        }

        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty()&& flow == RegisterFormActivity.TYPE_CONTRACT ){
            whenContractInvalid.postValue(true)
            valid = false
        }

        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty()
                && ( flow == RegisterFormActivity.TYPE_PASSPORT ||  flow == RegisterFormActivity.TYPE_ID )){
            whenIdCardInvalid.postValue(true)
            valid = false
        }

        return valid
    }

    fun getPhonenumberList(email: String, simplyId: String, flow: String) {
        if (!formValidate(email, simplyId, flow)) {
            return
        }

        //DatabaseManager.getInstance().saveRegisterData(registerState)

        val request = if (FlowManager.isRegisterFlow()) {
            GetPhonenumberRequest.build("", "")
        } else {
            GetPhonenumberRequest.buildForForgotPincode("", "")
        }

        whenLoading.value = true

        TLTApiManager.getInstance()
                .getPhonenumber(request) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        whenGetPhoneNumberError.postValue(true)
                        return@getPhonenumber
                    }

                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemPhoneNumberResponse>::class.java)
                            .toList()
                            .map { PhoneNumber(it.keyPhone, it.phonenumber) }

                    whenPhoneNumberListLoaded.postValue(ArrayList(items))
                }
    }

    private fun prepareData(request: BeforeRegisterRequest) {
        whenLoading.postValue(false)
        whenPrepareDataLoaded.postValue(true)

        TLTApiManager.getInstance().beforeRegister(request) { _, _ ->

        }
    }

    data class Model(
            val email: String = "",
            val idCard: String = "",
            val birthdate: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}