package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.request.SyncIdCardRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class BasicIDCardFrontViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedStep = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()

    fun SyncIDCard( imgStr : String , refNo : String  ) {
        val request = SyncIdCardRequest.build( strImg = imgStr , refID = refNo  )
        whenLoading.value = true
        apiLoanManager.syncIdCard(request) {isError, result , step, msg  ->
            whenLoading.value = false
                 if (isError) {
                    whenLoading.value = false
                    if (result != "device logon") {
                        whenDataLoadedFailure.value = result
                    }
                    return@syncIdCard
                }

                val item = JsonMapperManager.getInstance()
                        .gson.fromJson(result, GetStepResponse::class.java)

                var data =  MenuStepData(
                        status = item.status ,
                        ref_id = item.ref_id ,
                        ref_url = item.ref_url ,
                        step = step)

                if(data.status == "N") {
                    whenLoading.value = false
                    whenSyncFailureShowMsg.postValue(data)
                    return@syncIdCard
                }else{
                    whenLoading.value = false
                    whenSyncSuccess.value = true
                    whenSyncSuccessData.postValue(data)
                 }
        }
    }

}