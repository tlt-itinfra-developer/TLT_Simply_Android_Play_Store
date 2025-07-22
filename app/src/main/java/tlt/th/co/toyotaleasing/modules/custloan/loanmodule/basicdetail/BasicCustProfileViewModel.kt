package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.basicdetail


import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class BasicCustProfileViewModel   : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()

    fun CheckStepAPI(refID : String)  {
        whenLoading.postValue(true)
        apiLoanManager
                .checkStep(CheckStepRequest.build(refID =  refID))  { isError, result, step, msg  ->
                    whenLoading.value = false
                    if (isError) {
                        whenLoading.value = false
                        if (result != "device logon") {
                            whenDataLoadedFailure.value = result
                        }
                        if(msg.length > 0 ) {
                            whenDataLoadedMessage.postValue(msg)
                        }
                        return@checkStep
                    }

                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
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
                        return@checkStep
                    }else{
                        whenLoading.value = false
                        whenSyncSuccess.value = true
                        whenSyncSuccessData.postValue(data)
                    }
                }

    }

}