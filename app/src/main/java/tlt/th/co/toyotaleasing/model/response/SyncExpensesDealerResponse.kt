package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class SyncExpensesDealerResponse {
    @SerializedName("APPOINTMENT")
    var aPPOINTMENT: String = ""
    @SerializedName("CAN_CHANGE")
    var cANCHANGE: String= ""
    @SerializedName("DUE_DATE")
    var dUEDATE: String= ""
    @SerializedName("SHOWROOM_CODE")
    var sHOWROOMCODE: String= ""
    @SerializedName("SHOWROOM_NAME")
    var sHOWROOMNAME: String = ""
    @SerializedName("CHANGE_DESC_TH")
    var cHANGEDESCTH: String = ""
    @SerializedName("CHANGE_DESC_EN")
    var cHANGEDESCEN: String = ""
    @SerializedName("CHANGE_STATUS_BTN_TH")
    var cHANGESTATUSBTNTH: String = ""
    @SerializedName("CHANGE_STATUS_BTN_EN")
    var cHANGESTATUSBTNEN: String = ""

    fun cHANGESTATUSBTN() = if (LocalizeManager.isThai()) {
        cHANGESTATUSBTNTH
    } else {
        cHANGESTATUSBTNEN
    }

    fun cHANGEDESC() = if (LocalizeManager.isThai()) {
        cHANGEDESCTH
    } else {
        cHANGEDESCEN
    }
}