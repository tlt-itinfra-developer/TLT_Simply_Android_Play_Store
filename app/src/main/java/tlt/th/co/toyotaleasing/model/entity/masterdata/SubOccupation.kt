package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class SubOccupation {

    @SerializedName("OC_ID")
    var oCID: String? = ""
    @SerializedName("SUBOC_CODE")
    var sUBOCCODE: String? = ""
    @SerializedName("SUBOC_NAME_EN")
    var sUBOCNAMEEN: String? = ""
    @SerializedName("SUBOC_NAME_TH")
    var sUBOCNAMETH: String? = ""


    fun getSubOCname(): String? {
        return if (LocalizeManager.isThai()) {
            sUBOCNAMETH
        } else {
            sUBOCNAMEEN
        }
    }
}