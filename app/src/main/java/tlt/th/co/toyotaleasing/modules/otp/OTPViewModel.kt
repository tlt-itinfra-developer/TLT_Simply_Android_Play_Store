package tlt.th.co.toyotaleasing.modules.otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.SendOTPRegisRequest
import tlt.th.co.toyotaleasing.model.request.VerifyOTPRegisRequest

class OTPViewModel : ViewModel() {

    val otpCountDownTimer = 300000L
    val otpIncorrectMessage = ContextManager.getInstance().getStringByRes(R.string.otp_entered_incorrect)
    val otpTimeoutMessage = ContextManager.getInstance().getStringByRes(R.string.otp_timeout_otp)

    val whenLoading = MutableLiveData<Boolean>()
    val whenSendOTPSuccess = MutableLiveData<String>()
    val whenSendOTPFailure = MutableLiveData<String>()
    val whenVerifyOTPSuccess = MutableLiveData<String>()
    val whenVerifyOTPFailure = MutableLiveData<String>()

    fun sendOTP() {
        if (BuildConfig.IS_AUTOMATE_TEST_MODE) {
            whenSendOTPSuccess.value = ""
            return
        }

        val phoneKey = ""
        val request = SendOTPRegisRequest.build(phoneKey)

        whenLoading.value = true

        TLTApiManager.getInstance().sendOTPRegister(request) { isError, result ->
            whenLoading.value = false

            when (isError) {
                true -> whenSendOTPFailure.value = result
                else -> whenSendOTPSuccess.value = result
            }
        }
    }

    fun verifyOTP(otp: String) {
        if (BuildConfig.IS_AUTOMATE_TEST_MODE) {
            whenVerifyOTPSuccess.value = ""
            return
        }

        val request = VerifyOTPRegisRequest.build(otp)

        whenLoading.value = true

        TLTApiManager.getInstance()
                .verifyOTPRegister(request) { isError, result ->
            whenLoading.value = false

            if (isError) {
                whenVerifyOTPFailure.value = result
                return@verifyOTPRegister
            }

            when (result) {
                "T" -> whenVerifyOTPFailure.value = otpTimeoutMessage
                "F" -> whenVerifyOTPFailure.value = otpIncorrectMessage
                "N" -> whenVerifyOTPFailure.value = otpIncorrectMessage
                else -> whenVerifyOTPSuccess.value = ""
            }
        }
    }

    fun isRegisterFlow() = FlowManager.isRegisterFlow()
}