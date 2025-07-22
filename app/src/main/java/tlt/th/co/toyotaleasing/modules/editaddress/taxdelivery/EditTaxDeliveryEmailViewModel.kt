package tlt.th.co.toyotaleasing.modules.editaddress.taxdelivery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.common.extension.isEmail
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.request.ChangeProfileRequest
import tlt.th.co.toyotaleasing.util.AppUtils

class EditTaxDeliveryEmailViewModel : ViewModel() {
    val whenLoading = MutableLiveData<Boolean>()
    val whenDataLoaded = MutableLiveData<Model>()
    val whenEmailInvalidFormat = MutableLiveData<Boolean>()
    val whenPhonenumberInvalidFormat = MutableLiveData<Boolean>()
    val whenSubmitFormSuccess = MutableLiveData<Boolean>()
    val whenSubmitFormFailure = MutableLiveData<String>()

    private val tax = CacheManager.getCacheTax()!!

    fun getInitialData() {

        val tax = CacheManager.getCacheTax()
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

        val request = ChangeProfileRequest.buildForContractEmailAndPhone(tax.eXTCONTRACT!!, email, phoneNumber)

        whenLoading.postValue(true)

        TLTApiManager.getInstance()
                .changeProfile(request) { isError, result ->
                    whenLoading.postValue(false)

                    if (isError) {
                        if (result != "device login") {
                            whenSubmitFormFailure.postValue(result)
                        }
                        return@changeProfile
                    }

                    whenSubmitFormSuccess.postValue(true)
                }
    }

    data class Model(
            val email: String = "",
            val phoneNumber: String = "",
            val isStaffApp: Boolean = AppUtils.isStaffApp()
    )
}