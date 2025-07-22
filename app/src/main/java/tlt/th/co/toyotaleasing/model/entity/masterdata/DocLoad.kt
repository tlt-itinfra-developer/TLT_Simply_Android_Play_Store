package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class DocLoad  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("DOC_ID")
    var dOCID: String? = ""
    @SerializedName("DOC_TYPE")
    var dOCTYPE: String? = ""
    @SerializedName("DOC_LINK")
    var dOCLINK: String? = ""
    @SerializedName("DOC_DESC_TH")
    var dOCDESCTH: String? = ""
    @SerializedName("DOC_DESC_EN")
    var dOCDESCEN: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun docDescription() = if (LocalizeManager.isThai()) {
        dOCDESCTH
    } else {
        dOCDESCEN
    }
}