package tlt.th.co.toyotaleasing.manager.api.tltloan

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tlt.th.co.toyotaleasing.common.eventbus.DeviceLogonEvent
import tlt.th.co.toyotaleasing.common.eventbus.NoInternetEvent
import tlt.th.co.toyotaleasing.common.eventbus.ServerUnAvailableEvent
import tlt.th.co.toyotaleasing.common.eventbus.SocketTimeoutEvent
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.TLTLoanResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class TLTLoanApiCallback<String> : Callback<String> {

    abstract fun onSuccess(jsonResult: kotlin.String , step : kotlin.String , msg : kotlin.String)
    abstract fun onFailure(message: kotlin.String, msg : kotlin.String = "" , isWorkable: Boolean = true)

    override fun onResponse(call: Call<String>?, response: Response<String>?) {
        if (response?.isSuccessful == false) {
            if (response.code() == 503) {
                onFailure(CASE_SERVER_UNAVAILABLE)
                sendEventToBaseActivity(ServerUnAvailableEvent())
            } else {
                onFailure(call, Exception(response.message()))
            }
            return
        }

        val tltLoanResponse = JsonMapperManager.getInstance()
                .gson.fromJson(response?.body() as kotlin.String, TLTLoanResponse::class.java)

        if (tltLoanResponse.isDeviceLogon()) {
            onFailure(tltLoanResponse.wsMsg.msgStatus)
            sendEventToBaseActivity(DeviceLogonEvent())
            return
        }

        if (tltLoanResponse.isInvalid()) {
            onFailure(tltLoanResponse.wsMsg.msgStatus)
            sendEventToBaseActivity(DeviceLogonEvent())
            return
        }

        if (tltLoanResponse.isAccessDenied()) {
            onFailure(tltLoanResponse.wsMsg.msgStatus)
            return
        }

        if (!tltLoanResponse.isSuccess()) {
            onFailure(tltLoanResponse.wsMsg.msgStatus,  tltLoanResponse.getMessage())
            return
        }

        UserManager.getInstance().saveAccessToken(tltLoanResponse.wsMsg.token)
        onSuccess(tltLoanResponse.getResult() , tltLoanResponse.getStep() , tltLoanResponse.getMessage())
    }

    override fun onFailure(call: Call<String>?, t: Throwable?) {
        if (isSocketTimeout(call, t)) {
            sendEventToBaseActivity(SocketTimeoutEvent())
            return
        }

        if (t is UnknownHostException) {
            onFailure(CASE_NO_INTERNET)
            sendEventNoInternetToBaseActivity(NoInternetEvent())
            return
        }

        onFailure(t?.message ?: "")
    }

    private fun isSocketTimeout(call: Call<String>?, t: Throwable?): Boolean {
        if (t !is SocketTimeoutException) {
            return false
        }

        call?.clone()?.enqueue(this)

        return true
    }

    private fun sendEventTimeoutToBaseActivity(event: Any) {
        BusManager.observe(event)
    }

    private fun sendEventNoInternetToBaseActivity(event: Any) {
        BusManager.observe(event)
    }

    private fun sendEventToBaseActivity(event: Any) {
        BusManager.observe(event)
    }

    companion object {
        const val CASE_NO_INTERNET = "noInternet"
        const val CASE_SERVER_UNAVAILABLE = "CASE_SERVER_UNAVAILABLE"
    }
}