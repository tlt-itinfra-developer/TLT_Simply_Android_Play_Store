package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class Hotline  {
    @SerializedName("TYPE_SYNC") var tYPESYNC: String? = ""
    @SerializedName("HOTLINE_ID") var hOTLINEID: String? = ""
    @SerializedName("HOT_NAME_TH") var hOTNAMETH: String? = ""
    @SerializedName("HOT_NAME_EN") var hOTNAMEEN: String? = ""
    @SerializedName("HOT_PHONE") var hOTPHONE: String? = ""
    @SerializedName("HOT_DESC") var hOTDESC: String? = ""
    @SerializedName("REC_ACTIVE") var rECACTIVE: String? = ""

    fun hOTNAME() : String? {
        return if (LocalizeManager.isThai()) {
            hOTNAMETH
        } else {
            hOTNAMEEN
        }
    }
}