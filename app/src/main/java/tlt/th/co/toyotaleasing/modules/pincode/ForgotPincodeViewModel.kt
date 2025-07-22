package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.common.extension.isDoesNotContainCharacter
import tlt.th.co.toyotaleasing.common.extension.isDoesNotContainSpecialCharacter
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.ForgotPincodeRequest
import tlt.th.co.toyotaleasing.model.request.GetPhonenumberRequest
import tlt.th.co.toyotaleasing.model.response.ItemPhoneNumberResponse
import tlt.th.co.toyotaleasing.modules.selectphone.PhoneNumber
import tlt.th.co.toyotaleasing.util.AppUtils

class ForgotPincodeViewModel : ViewModel() {

    val apiManager = TLTApiManager.getInstance()

    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenFormInvalid = MutableLiveData<Boolean>()
    val whenIdCardInvalid = MutableLiveData<Boolean>()
    val whenContractInvalid = MutableLiveData<Boolean>()
    val whenSimplyCardInvalid = MutableLiveData<Boolean>()
    val whenForgotPincideFalure = MutableLiveData<String>()
    val whenForgotPincideSuccess = MutableLiveData<Boolean>()
    val whenPhoneNumberListLoaded = MutableLiveData<ArrayList<PhoneNumber>>()

    fun getData() {
        val idcard = ""

        whenDataLoaded.value = Model(idcard)
    }

    fun formSubmit(idCard: String , flow : String) {
        if (!formValidate(idCard , flow )) {
            return
        }

        whenLoading.value = true

        apiManager.forgotPincode(ForgotPincodeRequest.build(idCard)) { isError, result ->
            whenLoading.value = false

            if (isError) {
                whenForgotPincideFalure.value = result
                whenForgotPincideFalure.postValue(result)
                return@forgotPincode
            }

            FlowManager.changeToForgotPinFlow()
            whenForgotPincideSuccess.value = true
        }
    }

    // by siri 25/10/2019
    private fun formValidate(simplyId: String, flow: String): Boolean {
        var valid = true


        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty() && flow == ForgotPincodeActivity.TYPE_SIMPLY ){
            whenSimplyCardInvalid.postValue(true)
            valid = false
        }

        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty()&& flow == ForgotPincodeActivity.TYPE_CONTRACT ){
            whenContractInvalid.postValue(true)
            valid = false
        }

        if (!simplyId.isDoesNotContainCharacter()
                && simplyId.isNullOrEmpty()
                && ( flow == ForgotPincodeActivity.TYPE_PASSPORT ||  flow == ForgotPincodeActivity.TYPE_ID )){
            whenIdCardInvalid.postValue(true)
            valid = false
        }

        return valid
    }

    fun getPhonenumberList() {


    }

    data class Model(
            val idCard: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}