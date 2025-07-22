package tlt.th.co.toyotaleasing.modules.editaddress.installment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.extension.isEmail
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.ChangeProfileRequest

class EditInstallmentEmailViewModel : ViewModel() {
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenEmailInvalidFormat = MutableLiveData<Boolean>()
    val whenPhonenumberInvalidFormat = MutableLiveData<Boolean>()
    val whenSubmitFormSuccess = MutableLiveData<Boolean>()
    val whenSubmitFormFailure = MutableLiveData<String>()

    private val installment = CacheManager.getCacheInstallment()!!
    private val carConr= ""

    fun getInitialData() {
        val data = Model(
                installment.eMAIL,
                installment.mOBILENO
        )

        whenDataLoaded.value = data
    }

    fun submitForm(email: String, phoneNumber: String) {
        if (!email.isEmail()) {
            whenEmailInvalidFormat.value = true
            return
        }

        if (phoneNumber.isEmpty()) {
            whenPhonenumberInvalidFormat.value = true
            return
        }

        val request = ChangeProfileRequest.buildForContractEmailAndPhone(installment.eXTCONTRACT, email, phoneNumber)

        whenLoading.value = true

        TLTApiManager.getInstance()
                .changeProfile(request) { isError, result ->
                    whenLoading.value = false

                    if (isError) {
                        if (result != "device logon") {
                            whenSubmitFormFailure.value = result
                        }
                        return@changeProfile
                    }

                    whenSubmitFormSuccess.value = true
                }
    }

    data class Model(
            val email: String = "",
            val phoneNumber: String = ""
    )
}