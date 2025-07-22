package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class AlertWording  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("WORDING_TYPE")
    var wORDINGTYPE: String? = ""
    @SerializedName("AW_CODE")
    var aWCODE: String? = ""
    @SerializedName("WORDING_TH")
    var wORDINGTH: String? = ""
    @SerializedName("WORDING_EN")
    var wORDINGEN: String? = ""
    @SerializedName("AW_ACCEPT_TH")
    var aWACCEPTTH: String? = ""
    @SerializedName("AW_DENY_TH")
    var aWDENYTH: String? = ""
    @SerializedName("AW_ACCEPT_EN")
    var aWACCEPTEN: String? = ""
    @SerializedName("AW_DENY_EN")
    var aWDENYEN: String? = ""
    @SerializedName("WORDING_DESC")
    var wORDINGDESC: String? = ""
    @SerializedName("AW_IMG_URL")
    var aWIMGURL: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun type() = if (LocalizeManager.isThai()) {
        aWDENYTH
    } else {
        aWDENYEN
    }

    fun wording() = if (LocalizeManager.isThai()) {
        wORDINGTH
    } else {
        wORDINGEN
    }

    fun awAccept() = if (LocalizeManager.isThai()) {
        aWACCEPTTH
    } else {
        aWACCEPTEN
    }

}