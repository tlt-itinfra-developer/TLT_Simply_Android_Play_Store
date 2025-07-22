package tlt.th.co.toyotaleasing.manager.api.tlt

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tlt.th.co.toyotaleasing.common.eventbus.*
import tlt.th.co.toyotaleasing.manager.BusManager
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.db.UserManager
import tlt.th.co.toyotaleasing.model.response.TLTResponse
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class TLTApiCallback<String> : Callback<String> {

    abstract fun onSuccess(jsonResult: kotlin.String)
    abstract fun onFailure(message: kotlin.String, isWorkable: Boolean = true)

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

        val tltResponse = JsonMapperManager.getInstance()
                .gson.fromJson(response?.body() as kotlin.String, TLTResponse::class.java)

        if (tltResponse.isDeviceLogon()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            sendEventToBaseActivity(DeviceLogonEvent())
            return
        }

        if (tltResponse.isInavalid()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            sendEventToBaseActivity(DeviceInvalidEvent())
            return
        }


        if (tltResponse.isAccessDenied()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            return
        }

        if (tltResponse.isLoginError()) {
            onFailure(tltResponse.getResult())
            return
        }

        if (tltResponse.isError()) {
            onFailure(tltResponse.wsMsg.msgStatus)
            return
        }



        UserManager.getInstance().saveAccessToken(tltResponse.wsMsg.token)
        onSuccess(tltResponse.getResult())
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