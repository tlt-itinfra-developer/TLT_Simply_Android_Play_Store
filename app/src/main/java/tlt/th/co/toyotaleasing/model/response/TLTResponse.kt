package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.JsonMapperManager

data class TLTResponse(
        @SerializedName("WSStatus")
        val status: String = "",
        @SerializedName("WSMsg")
        val wsMsg: Result
) {
    fun isSuccess() = status == "Y" && wsMsg.msgStatus == "success"

    fun isAccessDenied() = status == "Y" && wsMsg.msgStatus == "access denied"

    fun isDeviceLogon() = wsMsg.msgStatus == "device logon"

    fun isInavalid() = wsMsg.msgStatus == "invalid"

    fun isLoginError() = status == "Y" && wsMsg.msgStatus == "error" && wsMsg.result != ""

    fun isError() = status == "Y" && wsMsg.msgStatus == "error" && wsMsg.result == ""

    fun getResult(): String {
        val result = wsMsg.result

        if (result is String) {
            return result
        }

        return JsonMapperManager.getInstance().gson.toJson(result)
    }

    data class Result(
            @SerializedName("MsgStatus", alternate = ["Msgstatus"])
            val msgStatus: String = "",
            @SerializedName(value = "result", alternate = ["Result"])
            val result: Any,
            @SerializedName("rslc", alternate = ["Rslc"])
            val token: String = ""
    )
}