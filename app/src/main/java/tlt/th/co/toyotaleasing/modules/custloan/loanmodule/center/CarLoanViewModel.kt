package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.model.entity.LoanDataManager
import tlt.th.co.toyotaleasing.model.request.CancelCarRequest
import tlt.th.co.toyotaleasing.model.request.CheckIdentityRequest
import tlt.th.co.toyotaleasing.model.request.CheckStepRequest
import tlt.th.co.toyotaleasing.model.request.GetCarsLoanRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.model.response.ItemCarsLoanResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import kotlin.coroutines.suspendCoroutine

class CarLoanViewModel  : BaseViewModel() {
    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenDataLoaded = MutableLiveData<List<CarsLoanItems>>()
    val whenDataLoadedNoData = MutableLiveData<Boolean>()
    private var cars_list : List<CarsLoanItems>? = null
    val whenCheckStep  = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val whenCheckIdentity  = MutableLiveData<Boolean>()
    val whenCheckIdentityByRegis  = MutableLiveData<Boolean>()

    fun ClearDataEntity(){
    }

    fun getDataCarsLoan(idcard: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                whenLoading.postValue(true)

                val carsLoan = getCarsLoan(idcard = idcard)

            } catch (e: ApiException) {
                whenDataFailure.postValue(e.message)
            }
            finally {
                whenLoading.postValue(false)
            }
        }
    }

    private suspend fun getCarsLoan(idcard: String ) = suspendCoroutine<ItemCarsLoanResponse?> {
        var request = GetCarsLoanRequest.build(idcard)
        whenLoading.postValue(true)
        apiLoanManager.mainScreen(request){ isError, result , step , msg ->
            try{
                whenLoading.postValue(false)
                if (isError) {
                    if(msg.length > 0 ) {
//                        whenDataLoadedMessage.postValue(msg)
                    }
                    return@mainScreen
                }

                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }

                try {
                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemCarsLoanResponse>::class.java)
                            .toList()
                            .map {
                                CarsLoanItems(
                                         cARIMAGE = it.cARIMAGE ?: ""
                                         ,cARGRADE = it.cARGRADE ?: ""
                                        ,cARMODEL = it.cARMODEL ?: ""
                                        ,cARPRICE = it.cARPRICE ?: ""
                                        , eXPIREDATE = it.eXPIREDATE ?: ""
                                        , rEFID = it.rEFID?: ""
                                        , rEFSTATUS = it.rEFSTATUS?: ""
                                        , sTAMPDATE = it.sTAMPDATE?: ""
                                         ,rESDES = it.getResDes()?: ""

                                ) }

                    cars_list = items
                    if(items.size == 0) {
                        whenDataLoadedNoData.postValue(true)
                    }else{
                        whenDataLoaded.postValue(cars_list )
                    }
                }catch ( e : Exception){
                    Log.d("Error Connect API : " , e.stackTrace.toString())
                }
                return@mainScreen
            }catch ( e : Exception ) {
                Log.e( "getCarsLoan : " , e.stackTrace.toString() )
            }
        }
    }

    data class CarsLoanItems(
       var cARIMAGE:  String = "",
       var cARMODEL:  String = "",
       var cARPRICE:  String = "",
       var cARGRADE:  String = "",
       var eXPIREDATE:  String = "",
       var rEFID:  String = "",
       var rEFSTATUS:  String = "",
       var sTAMPDATE:  String = "",
       var rESDES: String =""
    )


    fun SetCancelCar(refID: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                whenLoading.postValue(true)

                val carsLoan = CancelCarsLoan(refID = refID)

            } catch (e: ApiException) {
                whenDataFailure.postValue(e.message)
            }
            finally {
                whenLoading.postValue(false)
            }
        }
    }

    private suspend fun CancelCarsLoan(refID: String ) = suspendCoroutine<ItemCarsLoanResponse?> {
        var request = CancelCarRequest.build(refID)
        whenLoading.value = true
        apiLoanManager.cancelCarsLoan(request){ isError, result , step, msg ->
            whenLoading.value = false
            try{
                if (isError) {
                    if(msg.length > 0 ) {
                        whenDataLoadedMessage.postValue(msg)
                    }
                    return@cancelCarsLoan
                }

                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }

                try {
                    val items = JsonMapperManager.getInstance()
                            .gson.fromJson(result, Array<ItemCarsLoanResponse>::class.java)
                            .toList()
                            .map {
                                CarsLoanItems(
                                        cARIMAGE = it.cARIMAGE ?: ""
                                        ,cARGRADE = it.cARGRADE ?: ""
                                        ,cARMODEL = it.cARMODEL ?: ""
                                        ,cARPRICE = it.cARPRICE ?: ""
                                        , eXPIREDATE = it.eXPIREDATE ?: ""
                                        , rEFID = it.rEFID?: ""
                                        , rEFSTATUS = it.rEFSTATUS?: ""
                                        , sTAMPDATE = it.sTAMPDATE?: ""
                                        ,rESDES = it.getResDes()?: ""

                                )
                            }
                    cars_list = items
                    if(items.size == 0) {
                        whenDataLoadedNoData.postValue(true)
                    }else{
                        whenDataLoaded.postValue(cars_list )
                    }
                }catch ( e : Exception){
                    Log.d("Error Connect API : " , e.stackTrace.toString())
                }
                return@cancelCarsLoan
            }catch ( e : Exception ) {
                Log.e( "CancelCarsLoan : " , e.stackTrace.toString() )
            }
        }
    }


    fun CheckStepAPI(refID : String)  {
        whenLoading.postValue(true)
        apiLoanManager
                .checkStep(CheckStepRequest.build(refID =  refID))  {isError, result , step, msg  ->
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


    fun getIdentity() {
        GlobalScope.launch(Dispatchers.Main) {
            apiLoanManager.getIdentity(CheckIdentityRequest.build(idcard = "",
                    refID = "")) { isError, result, step, msg ->
                if (isError) {
                    return@getIdentity
                }

                if (result == "Y") {
                    whenCheckIdentity.value = true
                } else {
                    whenCheckIdentity.value = false
                }
                return@getIdentity
            }
        }
    }

    fun getIdentityByRegister() {
        GlobalScope.launch(Dispatchers.Main) {
            apiLoanManager.getIdentity(CheckIdentityRequest.build(idcard = "",
                    refID = "")) { isError, result, step, msg ->
                if (isError) {
                    return@getIdentity
                }

                if (result == "Y") {
                    whenCheckIdentityByRegis.value = true
                } else {
                    whenCheckIdentityByRegis.value = false
                }
                return@getIdentity
            }
        }
    }

}