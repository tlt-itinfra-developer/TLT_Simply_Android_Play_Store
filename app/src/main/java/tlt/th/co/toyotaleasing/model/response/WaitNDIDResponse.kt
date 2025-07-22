package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class WaitNDIDResponse {
    @SerializedName("NDID_REF_ID")
    var nDIDREFID: String = ""
    @SerializedName("REF_ID")
    var rEFID: String  = ""
    @SerializedName("Button_TH")
    var bUTTONTH: String= ""
    @SerializedName("Button_EN")
    var bUTTONEN: String= ""
    @SerializedName("Desc_TH")
    var dESCTH: String= ""
    @SerializedName("Desc_EN")
    var dESCEN: String= ""
    @SerializedName("Descstatus")
    var dESCSTATUS: String= ""
    @SerializedName("Request_msg_TH")
    var rEQUESTMSGTH: String= ""
    @SerializedName("Request_msg_EN")
    var rEQUESTMSGEN: String= ""

    fun dESC() : String {
        return if (LocalizeManager.isThai()) {
            dESCTH
        } else {
            dESCEN
        }
    }
    fun bTTON() : String {
        return if (LocalizeManager.isThai()) {
            bUTTONTH
        } else {
            bUTTONEN
        }
    }

    fun rEQUESTMSG() : String {
        return if (LocalizeManager.isThai()) {
            rEQUESTMSGTH
        } else {
            rEQUESTMSGEN
        }
    }
}



