package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class SyncInfoResponse{
    @SerializedName("COUNT")
    var cOUNT: String = ""
    @SerializedName("DOPA_DESC_EN")
    var dOPADESCEN: String= ""
    @SerializedName("DOPA_DESC_TH")
    var dOPADESCTH: String= ""
    @SerializedName("REF_ID")
    var rEFID: String= ""
    @SerializedName("STATUS")
    var sTATUS: String= ""

    fun dopaDesc() = if (LocalizeManager.isThai()) {
        dOPADESCTH
    } else {
        dOPADESCEN
    }
}