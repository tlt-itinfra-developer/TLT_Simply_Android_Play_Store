package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.request.DecisionEngineResultRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class CInfoCreditConsiderationViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    fun CheckStepAPI(refID : String)  {
//        whenLoading.postValue(true)
        apiLoanManager.checkStep(CheckStepRequest.build(refID =  refID))  { isError, result, step, msg  ->

//            whenLoading.value = false
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                return@checkStep
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

    fun getDecisionEngine(refNo : String = "") {
        val request = DecisionEngineResultRequest.build(ref_no = refNo)
        apiLoanManager.decisionEngine(request) { isError, result, step, msg ->
            if (isError) {
                if (result != "device logon") {
//                    whenDataLoadedFailure.value = result
                }
//                if (msg.length > 0) {
//                    whenDataLoadedMessage.postValue(msg)
//                }
                return@decisionEngine
            }

        }
    }
    data class Model(
            val item: String = ""
    )
}