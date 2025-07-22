package tlt.th.co.toyotaleasing.modules.fingerprint

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.SetTouchIDRequest

class FingerprintSettingViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenSetupFingerprintSuccess = MutableLiveData<String>()
    val whenSetupFingerprintFailure = MutableLiveData<String>()

    fun setupFingerprintAuth(request: SetTouchIDRequest) {
        whenLoading.postValue(true)

        TLTApiManager.getInstance()
                .setTouchID(request) { isError, result ->
                    whenLoading.postValue(false)

                    if (isError) {
                        if (result != "device logon") {
                            whenSetupFingerprintFailure.value = result
                        }
                        return@setTouchID
                    }

                    if (request.pselect == "Y") {
                        UserManager.getInstance().enableFingerprintAuth()
                    }

                    whenSetupFingerprintSuccess.value = result
                }
    }
}