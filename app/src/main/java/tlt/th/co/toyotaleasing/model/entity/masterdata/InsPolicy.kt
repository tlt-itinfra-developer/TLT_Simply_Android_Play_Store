package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class InsPolicy  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("INSU_ID")
    var iNSUID: String? = ""
    @SerializedName("INSU_INDEX")
    var iNSUINDEX: String? = ""
    @SerializedName("INSU_TYPE_TH")
    var iNSUTYPETH: String? = ""
    @SerializedName("INSU_NAME_TH")
    var iNSUNAMETH: String? = ""
    @SerializedName("INSU_TYPE_EN")
    var iNSUTYPEEN: String? = ""
    @SerializedName("INSU_NAME_EN")
    var iNSUNAMEEN: String? = ""
    @SerializedName("INSU_DESC_TH")
    var iNSUDESCTH: String? = ""
    @SerializedName("INSU_DESC_EN")
    var iNSUDESCEN: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun getName() = if (LocalizeManager.isThai()) {
        iNSUTYPETH
    } else {
        iNSUTYPEEN
    }

    fun getDescription() = if (LocalizeManager.isThai()) {
        iNSUDESCTH
    } else {
        iNSUDESCEN
    }
}