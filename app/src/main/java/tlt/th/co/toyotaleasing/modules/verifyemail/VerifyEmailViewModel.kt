package tlt.th.co.toyotaleasing.modules.verifyemail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.model.request.SendEmailRegisterRequest

class VerifyEmailViewModel : ViewModel() {

    val whenGetDefaultEmail = MutableLiveData<String>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenResendMailFailure = MutableLiveData<String>()
    val whenResendMailSuccess = MutableLiveData<String>()
    val whenVerifyEmailSuccess = MutableLiveData<String>()

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
                                        whenResendMailFailure.value = result
                                    }
                                    return@sendEmailRegister
                                }

                                whenResendMailSuccess.value = ""
                            }
                }
    }

    private fun sendEmailRegister(email: String) {
        whenLoading.postValue(true)
        val sendEmailRequest = SendEmailRegisterRequest.build(email)

        TLTApiManager.getInstance().sendEmailRegister(sendEmailRequest) { isError, result ->
            whenLoading.postValue(false)

            if (isError) {
                return@sendEmailRegister
            }

        }
    }

    fun checkVerifyEmail() {
        TLTApiManager.getInstance().checkVerifyEmail { isError, result ->
            if (isError) {
                return@checkVerifyEmail
            }

            whenVerifyEmailSuccess.postValue(result)
        }
    }

    fun setEmail(email: String) {
        this.whenGetDefaultEmail.value = email
        sendEmailRegister(email = email)
    }
}