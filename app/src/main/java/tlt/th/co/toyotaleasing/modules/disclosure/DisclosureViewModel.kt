package tlt.th.co.toyotaleasing.modules.disclosure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.AcceptTermConditionRequest

class DisclosureViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenUserAllow = MutableLiveData<String>()
    val whenUserDenied = MutableLiveData<String>()

    fun sendDisclosure(isAccept: Boolean) {
        whenLoading.postValue(true)

        val termAndConditionList = MasterDataManager.getInstance().getDisclosureList()


        val code = ""
        val request = if (isAccept) {
            AcceptTermConditionRequest.buildForAccept(code)
        } else {
            AcceptTermConditionRequest.buildForDenied(code)
        }

        TLTApiManager.getInstance()
                .acceptTermCondition(request) { isError, result ->
                    whenLoading.postValue(false)

                    when (isError) {
                        true -> whenUserDenied.value = result
                        else -> whenUserAllow.value = result
                    }
                }
    }
}