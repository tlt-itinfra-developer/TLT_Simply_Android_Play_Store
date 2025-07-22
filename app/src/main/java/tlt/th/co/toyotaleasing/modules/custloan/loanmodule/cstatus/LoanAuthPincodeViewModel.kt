package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.cstatus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import tlt.th.co.toyotaleasing.R
import tlt.th.co.toyotaleasing.manager.ContextManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocationManager
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiCallback
import tlt.th.co.toyotaleasing.manager.api.tlt.TLTApiManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.*
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoanAuthPincodeViewModel : ViewModel() {

    private val userManager = UserManager.getInstance()
    val whenDataLoadedMessage = MutableLiveData<String>()
    val authAttemptOverLimitMessage = ContextManager.getInstance().getStringByRes(R.string.auth_attempt_over_limit_dialog_description)

    val whenLoading = MutableLiveData<Boolean>()
    val whenAuthFailure = MutableLiveData<String>()
    val whenAuthAttemptOverLimit = MutableLiveData<Boolean>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()


    fun checkLoginAttemptOverLimit() {
        if (!userManager.isLoginAttemptOverLimit()) {
            return
        }
        whenAuthFailure.postValue(authAttemptOverLimitMessage)
        whenAuthAttemptOverLimit.postValue(true)
    }

    private suspend fun hashAuthPincode(pincode: String) = suspendCoroutine<String> {
        if (pincode.isEmpty()) {
            it.resume("")
            return@suspendCoroutine
        }

        val hash = UserManager.getInstance().hashPincodeForAuth(pincode)
        it.resume(hash)
    }


    fun SyncSubmitEContract(refID : String , pincode: String)  {
        whenLoading.postValue(true)

        GlobalScope.launch(Dispatchers.Main) {
            val hash = hashAuthPincode(pincode)
            TLTLoanApiManager.getInstance()
                    .SyncSubmitEContract(EContractRequest.build(refID =  refID , pincode = hash))  { isError, result, step, msg  ->
                        whenLoading.postValue(false)
                        if (isError) {
                            if(msg.length > 0 ) {
                                whenDataLoadedMessage.postValue(msg)
                            }
                            return@SyncSubmitEContract
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
                            return@SyncSubmitEContract
                        }else{
                            whenLoading.value = false
                            whenSyncSuccess.value = true
                            whenSyncSuccessData.postValue(data)
                        }

                    }
        }

    }

    data class Model(
            val isShowForceUpdatePopup: Boolean = false,
            val forceUpdateMessage: String = "",
            val isShowNoticePopup: Boolean = false,
            val NoticeMessage: String = ""
    )
}