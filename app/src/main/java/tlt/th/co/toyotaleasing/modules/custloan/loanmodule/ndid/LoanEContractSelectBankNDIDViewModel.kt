package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.GetListIDPRequest
import tlt.th.co.toyotaleasing.model.request.NDIDRequest
import tlt.th.co.toyotaleasing.model.response.IDPListResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class LoanEContractSelectBankNDIDViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataIDPLoaded = MutableLiveData<List<IDPListModel>>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenDataLoadedStep = MutableLiveData<String>()

    fun getListDP(  refID : String , ndidID : String) {
//        whenLoading.value = true
        var request = GetListIDPRequest.build( ref_id =  refID , ndid_id =  ndidID)
        apiLoanManager.getListIDPEContract(request ){ isError, result, step, msg ->
//            whenLoading.value = false
            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@getListIDPEContract
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<IDPListResponse>::class.java)

            var dPList =  item.map {
                    IDPListModel(
                            nodeId = it.nodeId ,
                            nodeImgUrl =  it.nodeImgUrl,
                            marketingName = it.NodeName().toString() ,
                            indCode = it.indCode ,
                            compCode = it.compCode)
            }

            whenDataIDPLoaded.postValue(dPList)
        }
    }

    fun submit(refID: String  , nodeID: String , indCode: String , compCode: String , ndidID: String  , tltmsg: String) {
        whenLoading.value = true
        var request = NDIDRequest.build(
                ref_id = refID,
                node_id = nodeID,
                ind_code = indCode,
                comp_code = compCode ,
                ndid_id = ndidID  ,
                tltMsg = tltmsg )
        apiLoanManager.requestNDIDEContract(request) { isError, result, step, msg ->
            whenLoading.value = false
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if (msg.length > 0) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@requestNDIDEContract
            }

            if (msg.length > 0) {
                whenDataLoadedMessage.postValue(msg)
            }

            whenDataLoadedStep.postValue(step)
        }
    }


    data class IDPListModel(
            var nodeId :  String = "" ,
            var indCode :  String = "" ,
            var compCode: String = "" ,
            var nodeImgUrl: String = "" ,
            var marketingName: String = ""
    )

}