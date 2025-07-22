package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.EContractRequest
import tlt.th.co.toyotaleasing.model.response.GetEContractResponse
import tlt.th.co.toyotaleasing.model.response.SyncExpenseResponse
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import tlt.th.co.toyotaleasing.util.PdfUtils

class OtherExpenseLoanViewModel : ViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDocLoaded = MutableLiveData<EcontractModel>()
    val whenLoading = MutableLiveData<Boolean>()
    val whenLinkLoaded = MutableLiveData<String>()

    fun getEContract( refID : String) {
        whenLoading.value = true
        val request = EContractRequest.build(refID , "")

        apiLoanManager.getEContract(request){ isError, result, step, msg ->
            whenLoading.value = false
            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@getEContract
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<GetEContractResponse>::class.java).get(0)

            var data =  EcontractModel(
                    pdfFile= item.pDFFILE ,
                    pdfName= item.pDFNAME
            )
            whenDocLoaded.postValue(data)
        }
    }

    fun getDigitalHandbook() {
        whenLinkLoaded.postValue("")
    }

    data class EcontractModel(
            var pdfFile:  String = "" ,
            var pdfName:  String = ""
    )


}