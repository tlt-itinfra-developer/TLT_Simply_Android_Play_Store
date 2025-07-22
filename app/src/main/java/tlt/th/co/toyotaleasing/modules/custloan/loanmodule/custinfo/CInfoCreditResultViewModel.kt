package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.custinfo

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.request.CancelDecisionEngineRequest
import tlt.th.co.toyotaleasing.model.request.DecisionEngineResultRequest
import tlt.th.co.toyotaleasing.model.request.SyncDecisionEngineRequest
import tlt.th.co.toyotaleasing.model.response.DecisionEngineResultResponse
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData

class CInfoCreditResultViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenDataLoaded = MutableLiveData<DecisionEngine>()


    fun getDecisionEngine(refNo : String = "") {
        val request = DecisionEngineResultRequest.build(ref_no = refNo)

        whenLoading.value = true

        apiLoanManager.decisionEngineResult(request) { isError, result, step, msg ->
            whenLoading.value = false

            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if (msg.length > 0) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@decisionEngineResult
            }

            if (msg.length > 0) {
                whenDataLoadedMessage.postValue(msg)
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, DecisionEngineResultResponse::class.java)

            if (items == null) {
                whenDataLoadedFailure.value = ""
                return@decisionEngineResult
            }
            if (items != null) {
                val datas = DecisionEngine(
                        flagOffer = items.flagOffer!!,
                        dofferList = items.dofferlist!!.map { dOffer(offerId = it.offerId!!, offerTextValue = it.offerValue()!!, flagSelect = false) },
                        cofferList =  cOffer(
                                  ctype = items.cofferlist!!.ctype!!
                                , ctypedesc = items.cofferlist!!.ctypedesc!!)
                                ,carDetail = carsItems(
                                 cARIMAGE = items.carDetail!!.cARIMAGE
                                ,cARGRADE = items.carDetail!!.cARGRADE
                                ,cARMODEL = items.carDetail!!.cARMODEL
                                ,cARPRICE = items.carDetail!!.cARPRICE
                                , eXPIREDATE = items.carDetail!!.eXPIREDATE
                                , rEFID = items.carDetail!!.rEFID
                                , rEFSTATUS = items.carDetail!!.rEFSTATUS
                                , rESDES = items.carDetail!!.getResDes()
                                , sTAMPDATE = items.carDetail!!.sTAMPDATE)
                                ,status = items.status!!
                                ,statusDetail = items.statusDetail()
                                ,statusHeader = items.statusHeader())
                whenDataLoaded.postValue(datas)
            }else{
                whenDataLoadedFailure.value = ""
                return@decisionEngineResult
            }
        }
    }

    fun CancelDecisionEngine(  engineType : String  , refID : String)  {
        whenLoading.postValue(true)
        apiLoanManager.cancelDecisionEngine(CancelDecisionEngineRequest.build( engineType =  engineType , refID =  refID))  { isError, result, step, msg  ->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                return@cancelDecisionEngine
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
                return@cancelDecisionEngine
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }

    fun SyncDecisionEngine(engineType : String  , offer_id : String = "", refID : String   )  {
        whenLoading.postValue(true)
        apiLoanManager.syncDecisionEngine(SyncDecisionEngineRequest.build( engineType = engineType ,
                                                                           offerID = offer_id ,
                                                                           refID =  refID))  { isError, result, step, msg  ->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                return@syncDecisionEngine
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
                return@syncDecisionEngine
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }

    data class DecisionEngine(
            val flagOffer: String = "",
            val dofferList: List<dOffer>,
            val cofferList: cOffer,
            val carDetail: carsItems,
            val status: String = "",
            val statusDetail: String = "",
            val statusHeader: String = ""
    )

    data class dOffer(
            var offerId: String = "",
            var offerTextValue: String  = "" ,
            var flagSelect: Boolean  = false
    )

    data class cOffer(
           var ctype: String,
           var ctypedesc: String
    )

    data class carsItems(
            var cARIMAGE:  String = "",
            var cARMODEL:  String = "",
            var cARPRICE:  String = "",
            var cARGRADE:  String = "",
            var eXPIREDATE:  String = "",
            var rEFID:  String = "",
            var rEFSTATUS:  String = "",
            var sTAMPDATE:  String = "" ,
            var rESDES: String =""
    )
}