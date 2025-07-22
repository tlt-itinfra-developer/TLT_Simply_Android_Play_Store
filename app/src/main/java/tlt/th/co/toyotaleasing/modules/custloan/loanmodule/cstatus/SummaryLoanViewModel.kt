package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import androidx.lifecycle.MutableLiveData
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.CacheManager
import tlt.th.co.toyotaleasing.model.response.ExpensesResponse
import tlt.th.co.toyotaleasing.model.request.GetExpenseRequest
import tlt.th.co.toyotaleasing.model.request.SyncExpenseRequest
import tlt.th.co.toyotaleasing.model.request.SyncExpenseDealerRequest
import tlt.th.co.toyotaleasing.model.response.SyncExpenseResponse
import tlt.th.co.toyotaleasing.model.response.SyncExpensesDealerResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus.common.OtherExpense

class SummaryLoanViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenDataLoaded = MutableLiveData<ExpensesDataModel>()
    val whenDataLoadedSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()

    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()

    val whenDataDealerChangeLoaded = MutableLiveData<DealerChangeModel>()
    val whenDataLoadedMessage = MutableLiveData<String>()

    val whenLoadPDFData = MutableLiveData<Boolean>()

    fun getDataVerify(refNo : String = "") {
        val request = GetExpenseRequest.build(ref_no = refNo)

        whenLoading.value = true

        apiLoanManager.expenses(request) {isError, result , step, msg  ->
            whenLoading.value = false

            if (isError) {
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@expenses
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<ExpensesResponse>::class.java)

            if (items.isEmpty()) {
                whenDataLoadedFailure.value = ""
                return@expenses
            }

            var data =  items.map { ExpensesDataModel(
                pROPOSALNAME =  it.mAIN.pROPOSALNAME ,
                pROPOSALNO =  it.mAIN.pROPOSALNO ,
                sTATUSAPP = it.mAIN.sTATUSAPP ,
                aPPOINTMENT = it.dELIVERCAR.aPPOINTMENT ,
                dUEDATE = it.dELIVERCAR.dUEDATE ,
                sHOWROOMCODE = it.dELIVERCAR.sHOWROOMCODE ,
                sHOWROOMNAME = it.dELIVERCAR.sHOWROOMNAME ,
                cANCHANGE = it.dELIVERCAR.cANCHANGE ,
                cHANGEDESC = it.dELIVERCAR.cHANGEDESC() ,
                cHANGESTATUSBTN = it.dELIVERCAR.cHANGESTATUSBTN() ,
                contractHandlingFee =  it.mCI.contractHandlingFee ,
                otherExpense =  it.mCI.otherexpense ,
                cARPRICE = it.mCI.cARPRICE ,
                dOWNPAYMENT = it.mCI.dOWNPAYMENT ,
                gRADE = it.mCI.gRADE ,
                iNSTALLMENT =it.mCI.iNSTALLMENT ,
                iNTERESTS = it.mCI.iNTERESTS ,
                mODEL = it.mCI.mODEL ,
                pAYMENTTERM = it.mCI.pAYMENTTERM ,
                Addr_pLAT = it.aDDRESS.pLAT ,
                Addr_pLONG = it.aDDRESS.pLONG ,
                Addr_rEALADDRESS = it.aDDRESS.rEALADDRESS ,
                Mailing_pLAT = it.mAILING.pLAT ,
                Mailing_pLON = it.mAILING.pLON ,
                Mailing_rEALADDRESS = it.mAILING.rEALADDRESS ,
                regis_pLAT = it.rEGIS.pLAT ,
                regis_pLONG =  it.rEGIS.pLONG ,
                regis_rEALADDRESS =  it.rEGIS.rEALADDRESS ,
                sCANQR = it.qRCODE.sCANQR ,
                OTHERDETAILLIST = it.mCI.oTHERDETAIL!!.map { OtherExpense(
                            itemName = it.itemname ,
                            itemAmt = it.itemamt
                    )
                }


            ) }


            whenDataLoadedSuccess.value = true
            whenDataLoaded.postValue(data.get(0))
        }
    }

    //    -- QR code --
    fun SyncExpense( refNo : String , qr_str :  String ) {
        val request = SyncExpenseRequest.build( refId = refNo , qrStr = qr_str)

        whenLoading.value = true
        apiLoanManager.syncExpenses(request) {isError, result , step, msg  ->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@syncExpenses
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }


            val items = JsonMapperManager.getInstance()
                    .gson.fromJson(result, SyncExpenseResponse::class.java)

            if (items == null ) {
                return@syncExpenses
            }

            CacheManager.cacheEContractQRCode(items)
            whenLoading.value = false
            whenSyncSuccess.value = true
            whenLoadPDFData.value = true

            //// MOVE ////
//
//            var filename = item.doc_name + "_" + refNo
//            var data = DocPDFModel(
//                    DOC_NAME =  filename,
//                    DOC_BASE64 = item.doc_base64 ,
//                    step = step)
////            PdfUtils.savePdf(filename, item.doc_base64  ?: "")
//
//            if(data.DOC_BASE64 == "") {
//                whenLoading.value = false
//                whenDataLoadedFailure.value = ""
//                return@syncExpenses
//            }else {
//                whenLoading.value = false
//                whenSyncSuccess.value = true
//                whenLoadPDFData.postValue(data)
//            }
        }
    }

    fun SyncExpenseDealerRequest( refNo : String , shw_code :  String , shw_name :  String ) {
        val request = SyncExpenseDealerRequest.build(ref_no = refNo ,
                                                                             showroom_code = shw_code ,
                                                                             showroom_name = shw_name)
        whenLoading.value = true
        apiLoanManager.syncExpensesDealer(request) {isError, result , step, msg  ->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@syncExpensesDealer
            }

            if(msg.length > 0 ) {
                whenDataLoadedMessage.postValue(msg)
            }

            val item = JsonMapperManager.getInstance()
                    .gson.fromJson(result, Array<SyncExpensesDealerResponse>::class.java).get(0)

            var data =  DealerChangeModel(
                        cANCHANGE = item.cANCHANGE,
                        cHANGEDESC = item.cHANGEDESC() ,
                        cHANGESTATUSBTN = item.cHANGESTATUSBTN(),
                        sHOWROOMCODE = item.sHOWROOMCODE,
                        sHOWROOMNAME = item.sHOWROOMNAME )

            whenDataDealerChangeLoaded.postValue(data)
        }
    }


    open class DealerChangeModel(
            var cANCHANGE: String = "",
            var cHANGEDESC: String = "",
            var cHANGESTATUSBTN: String = "",
            var sHOWROOMCODE: String = "" ,
            var sHOWROOMNAME: String = ""
    )


    open class ExpensesDataModel(
            var pROPOSALNAME: String= "",
            var pROPOSALNO: String= "",
            var sTATUSAPP: String,
            var aPPOINTMENT: String= "",
            var dUEDATE: String= "",
            var sHOWROOMCODE: String= "",
            var sHOWROOMNAME: String= "",
            var cANCHANGE: String,
            var cHANGEDESC: String,
            var cHANGESTATUSBTN: String,
            var cARPRICE: String= "",
            var dOWNPAYMENT: String= "",
            var gRADE: String= "",
            var iNSTALLMENT: String= "",
            var iNTERESTS: String= "",
            var mODEL: String= "",
            var pAYMENTTERM: String  = "",
            var contractHandlingFee: String,
            var otherExpense: String,
            var Addr_pLAT: String= "",
            var Addr_pLONG: String= "",
            var Addr_rEALADDRESS: String,
            var Mailing_pLAT: String= "",
            var Mailing_pLON: String= "",
            var Mailing_rEALADDRESS: String= "",
            var regis_pLAT: String= "",
            var regis_pLONG: String= "",
            var regis_rEALADDRESS: String = "",
            var sCANQR: String = "",
            var OTHERDETAILLIST: List<OtherExpense>
    )

    data class DocPDFModel(
           var DOC_NAME : String= "" ,
           var DOC_BASE64 : String= "" ,
           var step : String= ""
    )

}