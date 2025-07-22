package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.request.GetWaitNDIDRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.model.response.WaitNDIDResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class LoanEContractWaitNDIDViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataAuthenLoaded = MutableLiveData<AuthenModel>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    fun getWaitNDID( refID : String) {
//        whenLoading.value = true

        val request = GetWaitNDIDRequest.build(refID)

        apiLoanManager.waitNDIDEContract(request){ isError, result, step, msg ->
//            whenLoading.value = false
            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@waitNDIDEContract
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, WaitNDIDResponse::class.java)

            var data =  AuthenModel(
                     nDIDREFID= item.nDIDREFID,
                     rEFID= item.rEFID,
                     bUTTON= item.bTTON(),
                     dESC= item.dESC(),
                     dESCSTATUS= item.dESCSTATUS ,
                     rEQUESTMSG = item.rEQUESTMSG() ,
                     rEQUESTMSGTH = item.rEQUESTMSGTH
            )
            whenDataAuthenLoaded.postValue(data)
        }
    }

    fun CheckStepAPI(refID : String , screenName : String)  {
        whenLoading.postValue(true)
        apiLoanManager
                .checkStep(CheckStepRequest.build(refID =  refID  , screen_name = screenName ))  { isError, result, step, msg  ->
                    whenLoading.value = false
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

    data class AuthenModel(
            var nDIDREFID:  String = "" ,
            var rEFID:  String = "" ,
            var bUTTON:  String = "" ,
            var dESC:  String = "" ,
            var dESCSTATUS:  String = "" ,
            var rEQUESTMSG:  String = "" ,
            var rEQUESTMSGTH:  String = ""
    )

}