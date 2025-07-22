package tlt.th.co.toyotaleasing.modules.verifyemail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.SendEmailRegisterRequest

class VerifyFailViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenGetDefaultEmail = MutableLiveData<String>()
    val whenResendEmailFailure = MutableLiveData<String>()
    val whenResendEmailSuccess = MutableLiveData<Boolean>()
    val whenVerifyEmailSuccess = MutableLiveData<String>()

    fun init() {
        val emailInDB = ""
        whenGetDefaultEmail.value = emailInDB
    }

    fun resendEmail() {
        val sendEmailRegisterRequest = SendEmailRegisterRequest.build(whenGetDefaultEmail.value!!)

        whenLoading.value = true

        TLTApiManager.getInstance()
                .custRegisLoad { isError, result ->
                    TLTApiManager.getInstance()
                            .sendEmailRegister(sendEmailRegisterRequest) { isError, result ->
                                whenLoading.value = false

                                if (isError) {
                                    if (result != "device logon") {
                                        whenResendEmailFailure.value = result
                                    }
                                    return@sendEmailRegister
                                }

                                whenResendEmailSuccess.value = true
                            }
                }
    }

    fun checkVerifyEmail() {
        TLTApiManager.getInstance().checkVerifyEmail { isError, result ->
            if (isError) {
                return@checkVerifyEmail
            }

            whenVerifyEmailSuccess.value = result
        }
    }
}