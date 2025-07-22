package tlt.th.co.toyotaleasing.modules.staff.termcon

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager

import tlt.th.co.toyotaleasing.model.request.AcceptTermConditionRequest

class StaffTermAndConditionViewModel : BaseViewModel() {

    val whenUserAllowSuccess = MutableLiveData<Boolean>()

    fun sendTermAndCondition() {
        whenLoading.postValue(true)

        val request = AcceptTermConditionRequest.buildForAccept(
                termConditionCode = ""
        )

        TLTApiManager.getInstance()
                .acceptTermCondition(request) { isError, result ->
                    whenLoading.postValue(false)
                    if (isError) {
                        return@acceptTermCondition
                    }

                    ////DatabaseManager.getInstance().updateStatusTermsAndConditionForStaff()
                    whenUserAllowSuccess.postValue(true)
                }
    }
}