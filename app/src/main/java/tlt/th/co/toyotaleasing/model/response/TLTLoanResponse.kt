package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.JsonMapperManager
import tlt.th.co.toyotaleasing.manager.LocalizeManager

data class TLTLoanResponse(
        @SerializedName("WSStatus")
        val status: String = "",
        @SerializedName("WSMsg")
        val wsMsg: Result
) {
    fun isSuccess() = status == "Y" && wsMsg.msgStatus == "success"

    fun isAccessDenied() = status == "Y" && wsMsg.msgStatus == "access denied"

    fun isDeviceLogon() = wsMsg.msgStatus == "device logon"

    fun isInvalid() = wsMsg.msgStatus == "invalid"

    fun isError() = status == "Y" && wsMsg.msgStatus == "error" && wsMsg.result == ""

    fun getResult(): String {
        val result = wsMsg.result

        if (result is String) {
            return result
        }

        return JsonMapperManager.getInstance().gson.toJson(result)
    }

    fun getStep(): String {
        val result = wsMsg.step

        if (result is String) {
            return result
        }

        return result
    }

    fun getMessage(): String {

//        var result = wsMsg.msgdesc
        var result = ""
        if (LocalizeManager.isThai()) {
            result = wsMsg.msgdescth
        }else{
            result = wsMsg.msgdescen
        }

        if (result is String) {
            return result
        }

        return result
    }

    data class Result(
            @SerializedName("MsgStatus", alternate = ["Msgstatus"])
            val msgStatus: String = "",
            @SerializedName(value = "result", alternate = ["Result"])
            val result: Any,
            @SerializedName("rslc", alternate = ["Rslc"])
            val token: String = "" ,
            @SerializedName("step", alternate = ["Step"])
            val step: String = "" ,
            @SerializedName("msgdescen", alternate = ["Msgdesc_en"])
            val msgdescen: String = "" ,
            @SerializedName("msgdescth", alternate = ["Msgdesc_th"])
            val msgdescth: String = ""
//            @SerializedName("msgdesc", alternate = ["Msgdesc"])
//            val msgdesc: String = ""
    )
}