package tlt.th.co.toyotaleasing.modules.pincode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.manager.FlowManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.ChangePincodeRequest
import tlt.th.co.toyotaleasing.model.request.SetPINRegisRequest
import java.util.regex.Pattern
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SetupPincodeViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenPasswordNotMatchMessage = MutableLiveData<Boolean>()
    val whenPasswordRequire6DigitsMessage = MutableLiveData<Boolean>()
    val whenValidatePasswordSuccess = MutableLiveData<String>()
    val whenValidatePasswordFailure = MutableLiveData<String>()
    val whenSetupPincodeSuccess = MutableLiveData<String>()
    val whenSetupPincodeFailure = MutableLiveData<String>()
    val whenVerifyPatternPincodeFail = MutableLiveData<Boolean>()

    private val apiManager = TLTApiManager.getInstance()

    fun sendPincodeToApi(pincode: String) {
        val hash = UserManager.getInstance().hashPincodeForSetup(pincode)

        val request = if (FlowManager.isRegisterFlow()) {
            SetPINRegisRequest.buildForRegister(hash)
        } else {
            SetPINRegisRequest.buildForForgotPincode(hash)
        }

        sendPincodeToApi(request)
    }

    fun changePincode(oldPincode: String, newPincode: String) {
        GlobalScope.launch(Dispatchers.Main) {
            whenLoading.postValue(true)

            val oldHashPincode = hashAuthPincode(oldPincode)
            val newHashPincode = hashSetupPincode(newPincode)
            val request = ChangePincodeRequest.build(oldHashPincode, newHashPincode)

            apiManager.changePincode(request) { isError, result ->
                whenLoading.postValue(false)

                if (isError) {
                    whenSetupPincodeFailure.postValue(result)
                    return@changePincode
                }

                whenSetupPincodeSuccess.postValue(result)
            }
        }
    }

    fun validatePassword(password: String, confirmPassword: String) {
        if (password.length < 6) {
            whenPasswordNotMatchMessage.postValue(true)
            return
        }

        if (password != confirmPassword) {
            whenPasswordNotMatchMessage.postValue(true)
            return
        }

        if (verifyPincode(password)) {
            whenVerifyPatternPincodeFail.postValue(true)
            return
        }

        whenValidatePasswordSuccess.value = ""
    }

    fun verifyPincode(pincode: String): Boolean {
        val pattern = Pattern.compile("((\\d)\\2{5,}|012345|123456|234567|345678|456789|567890|098765|987654|876543|765432|654321|543210)")
        val matcher = pattern.matcher(pincode)
        return matcher.find()
    }

    private fun sendPincodeToApi(request: SetPINRegisRequest) {
        whenLoading.postValue(true)

        apiManager.setPinRegister(request) { isError, result ->
            whenLoading.postValue(false)

            when (isError) {
                true -> whenSetupPincodeFailure.postValue(result)
                else -> {
                    UserManager.getInstance().changeToCustomer()
                    whenSetupPincodeSuccess.postValue(result)
                }
            }
        }
    }

    private suspend fun hashAuthPincode(pincode: String) = suspendCoroutine<String> {
        if (pincode.isEmpty()) {
            it.resume("")
            return@suspendCoroutine
        }

        val hash = UserManager.getInstance().hashPincodeForAuth(pincode)
        it.resume(hash)
    }

    private suspend fun hashSetupPincode(pincode: String) = suspendCoroutine<String> {
        if (pincode.isEmpty()) {
            it.resume("")
            return@suspendCoroutine
        }

        val hash = UserManager.getInstance().hashPincodeForSetup(pincode)
        it.resume(hash)
    }

    fun isRegisterFlow() = FlowManager.isRegisterFlow()
    fun isResetPincodeFlow() = FlowManager.isResetPinFlow()
}