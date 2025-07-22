package tlt.th.co.toyotaleasing.modules.termcondition

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.AcceptTermConditionRequest

class TermAndConditionViewModel : ViewModel() {

    val whenLoading = MutableLiveData<Boolean>()
    val whenUserAllow = MutableLiveData<String>()
    val whenUserDenied = MutableLiveData<String>()

    fun sendTermCondition(isAccept: Boolean) {
        whenLoading.value = true

        val termAndConditionList = MasterDataManager.getInstance().getTermAndConditionList()

        val termAndConditionCode = ""
        val request = if (isAccept) {
            AcceptTermConditionRequest.buildForAccept(termAndConditionCode)
        } else {
            AcceptTermConditionRequest.buildForDenied(termAndConditionCode)
        }

        TLTApiManager.getInstance()
                .acceptTermCondition(request) { isError, result ->
                    whenLoading.value = false

                    when (isError) {
                        true -> whenUserDenied.value = result
                        else -> whenUserAllow.value = result
                    }
                }
    }
}