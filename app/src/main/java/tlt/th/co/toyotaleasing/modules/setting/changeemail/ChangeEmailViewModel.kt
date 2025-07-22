package tlt.th.co.toyotaleasing.modules.setting.changeemail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.common.extension.isEmail
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.ChangeProfileRequest

class ChangeEmailViewModel : BaseViewModel() {

    val whenEmailInvalidFormat = MutableLiveData<Boolean>()
    val whenChangeEmailSuccess = MutableLiveData<Boolean>()
    val whenSubmitFormFailure = MutableLiveData<String>()

    private val apiManager = TLTApiManager.getInstance()

    fun changeEmail(email: String = "") {

        if (!email.isEmail()) {
            whenEmailInvalidFormat.postValue(true)
            return
        }

        val request = ChangeProfileRequest.buildForChangeEmail(
                "",
                email
        )

        apiManager.changeProfile(request) { isError, result ->
            if (isError) {
                whenSubmitFormFailure.postValue(result)
                return@changeProfile
            }

            whenChangeEmailSuccess.postValue(true)
        }

    }

}