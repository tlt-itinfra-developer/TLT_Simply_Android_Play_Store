package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.carloan

import androidx.lifecycle.MutableLiveData
import android.util.Log
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.MasterDataManager
import tlt.th.co.toyotaleasing.model.request.GetDocSumUploadedRequest
import tlt.th.co.toyotaleasing.model.request.SubmitDocUploadRequest
import tlt.th.co.toyotaleasing.model.response.DocSumUploadedResponse
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class LoanUploadDocumentViewModel  : BaseViewModel() {

    private val masterDataManager = MasterDataManager.getInstance()
    val whenDataLoaded = MutableLiveData<List<DocItem>>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoadedMessage = MutableLiveData<String>()

    fun getDocType(summary: List<DocSummaryloadedItem>) {
        whenLoading.postValue(true)

        val docUpload = masterDataManager.getSyncDocTypeList()?.map {
            DocItem(
                dOCTYPE = it.dOCTYPE ?: "",
                dOCDESC = it.getDocDes() ?: "",
                max = it.mAXUPLOAD ?: "",
                min = it.mINUPLOAD ?: "",
                dOCREMARK = it.dOCREMARK ?: "",
                summary = ""
            )
        }!!.filter { it.dOCREMARK == "IMG" }

        for (data in docUpload) {
            summary.forEach {
                if (it.dOCTYPE == data.dOCTYPE) {
                    data.summary = it.sUMMARY
                }
            }
        }

        whenLoading.postValue(false)
        whenDataLoaded.postValue(docUpload)
    }

    fun getSumDocumentRequire( refNo : String ) {
                try {

                    val res = DocSumUploadedResponse.mockList()

                    val items =  res.map {
                        DocSummaryloadedItem(
                                dOCTYPE = it.dOCTYPE ?: ""
                                , sUMMARY = it.sUMMARY ?: ""
                        )
                    }

                    getDocType(items)
                }catch ( e : Exception){
                    whenLoading.postValue(false)
                    Log.d("Error Connect API : " , e.stackTrace.toString())
                }
    }

    fun SubmitDocUpload( refNo : String ) {
        val request = SubmitDocUploadRequest.build(ref_no = refNo )
        whenLoading.value = true
        apiLoanManager.submitDocUpload(request) {isError, result , step, msg  ->
            whenLoading.value = false
            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@submitDocUpload
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
                return@submitDocUpload
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }

    data class DocItem(
            val dOCTYPE: String = "",
            val dOCDESC: String = "",
            val max: String = "",
            val min:  String = "",
            val dOCREMARK :  String = "",
            var summary: String = ""

    )

    data class DocSummaryloadedItem(
            var dOCTYPE: String = "" ,
            var sUMMARY: String = ""
    )
}