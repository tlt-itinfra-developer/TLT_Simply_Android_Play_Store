package tlt.th.co.toyotaleasing.modules.otp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.BuildConfig
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager

import tlt.th.co.toyotaleasing.model.request.SendOnlineOTPRequest
import tlt.th.co.toyotaleasing.model.request.VerifyOnlineOTPRequest

class LoanOTPViewModel : ViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val otpCountDownTimer = 300000L
    val otpIncorrectMessage = ContextManager.getInstance().getStringByRes(R.string.otp_entered_incorrect)
    val otpTimeoutMessage = ContextManager.getInstance().getStringByRes(R.string.otp_timeout_otp)

    val whenLoading = MutableLiveData<Boolean>()
    val whenSendOTPSuccess = MutableLiveData<String>()
    val whenSendOTPFailure = MutableLiveData<String>()
    val whenVerifyOTPSuccess = MutableLiveData<String>()
    val whenVerifyOTPFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    fun sendOTP() {
        if (BuildConfig.IS_AUTOMATE_TEST_MODE) {
            whenSendOTPSuccess.value = ""
            return
        }

        val phoneKey =  ""
        val request = SendOnlineOTPRequest.build(phoneKey)

        whenLoading.value = true

        apiLoanManager.sendOnlineOTP(request) { isError, result, step , msg->
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

        val request = VerifyOnlineOTPRequest.build(otp)

        whenLoading.value = true

        apiLoanManager
                .verifyOnlineOTP(request) { isError, result , step , msg->
                    whenLoading.value = false

                    if (isError) {
                        whenVerifyOTPFailure.value = result
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        return@verifyOnlineOTP
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }

                    when (result) {
                        "T" -> whenVerifyOTPFailure.value = otpTimeoutMessage
                        "F" -> whenVerifyOTPFailure.value = otpIncorrectMessage
                        "N" -> whenVerifyOTPFailure.value = otpIncorrectMessage
                        else -> whenVerifyOTPSuccess.value = ""
                    }
                }
    }

}