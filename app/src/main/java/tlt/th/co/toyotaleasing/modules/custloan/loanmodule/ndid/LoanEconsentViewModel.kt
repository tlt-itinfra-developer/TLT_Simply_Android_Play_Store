package tlt.th.co.toyotaleasing.modules.custloan.loanmodule.ndid

import androidx.lifecycle.MutableLiveData
import android.util.Log
import okhttp3.*
import tlt.th.co.toyotaleasing.analytics.AnalyticsManager
import tlt.th.co.toyotaleasing.analytics.AnalyticsScreenName
import tlt.th.co.toyotaleasing.common.base.BaseViewModel
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager
import tlt.th.co.toyotaleasing.manager.api.tltloan.TLTLoanApiManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.request.EConsenetAcceptTermsConditionRequest
import tlt.th.co.toyotaleasing.model.request.EConsentNDIDRequest
import tlt.th.co.toyotaleasing.model.response.GetStepResponse
import tlt.th.co.toyotaleasing.modules.custloan.loanmodule.center.common.MenuStepData
import java.io.IOException

class LoanEconsentViewModel  : BaseViewModel() {

    private val apiLoanManager = TLTLoanApiManager.getInstance()
    val whenSyncSuccessData = MutableLiveData<MenuStepData>()
    val whenSyncFailureShowMsg = MutableLiveData<MenuStepData>()
    val whenSyncSuccess = MutableLiveData<Boolean>()
    val whenDataLoadedFailure = MutableLiveData<String>()
    val whenDataLoadedMessage = MutableLiveData<String>()
    private val profile = UserManager.getInstance().getProfile()
    val whenDataLoaded = MutableLiveData<String>()
    val whenDataLoadFail = MutableLiveData<Boolean>()

    fun GetData(refID : String , url : String ) {
        var jsonData: String = ""
        val accessToken = UserManager.getInstance().getProfile().token
        val language = if (LocalizeManager.isThai()) {
            "TH"
        } else {
            "EN"
        }
        val loanEconsentRequest = EConsentNDIDRequest.build(
                refID = refID,
                token = "Basic ${profile.token}"
        )
        jsonData = JsonMapperManager.getInstance().gson.toJson(loanEconsentRequest)

        val client = OkHttpClient()
        val mediaType = MediaType.parse("application/json")
        val body = RequestBody.create(mediaType, jsonData)
        val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "text/xml")
                .addHeader("Authorization", "Basic $accessToken")
                .addHeader("online_language", language)
                .addHeader("online_ref", refID)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                whenDataLoaded.postValue(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                try{
                    val body = response.body()
                    if (body != null) {
                        try {
                            //Use it anytime you want
                            val responseString = body.string()
                            whenDataLoaded.postValue(responseString)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                    }
                }catch (e : Exception){
                    Log.e("ECONSENT CONNECT FAIL", e.message.toString())
                    Log.e("ECONSENT CONNECT FAIL", e.stackTrace.toString())
                    whenDataLoadFail.postValue(true)
                }
            }
        })
    }


    fun sendEConsentCondition(isAccept: Boolean , refID : String ) {
        whenLoading.value = true

        val request = if (isAccept) {
            AnalyticsManager.onlineEConsentAccept()
            EConsenetAcceptTermsConditionRequest.buildForAccept(refID)
        } else {
            AnalyticsManager.onlineEConsentUnAccept()
            EConsenetAcceptTermsConditionRequest.buildForDenied(refID)
        }

        apiLoanManager.econsentAcceptTermsCondition(request) { isError, result , step , msg->
            if (isError) {
                whenLoading.value = false
                if (result != "device logon") {
                    whenDataLoadedFailure.value = result
                }
                if(msg.length > 0 ) {
                    whenDataLoadedMessage.postValue(msg)
                }
                return@econsentAcceptTermsCondition
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
                return@econsentAcceptTermsCondition
            }else{
                whenLoading.value = false
                whenSyncSuccess.value = true
                whenSyncSuccessData.postValue(data)
            }
        }
    }
}