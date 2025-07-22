package tlt.th.co.toyotaleasing.model.entity.masterdata


import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class SyncImage  {
    @SerializedName("DOC_DESC_EN")
    var dOCDESCEN: String? = ""
    @SerializedName("DOC_DESC_TH")
    var dOCDESCTH: String? = ""
    @SerializedName("DOC_REMARK")
    var dOCREMARK: String? = ""
    @SerializedName("DOC_TYPE")
    var dOCTYPE: String? = ""
    @SerializedName("MAX_UPLOAD")
    var mAXUPLOAD: String? = ""
    @SerializedName("MIN_UPLOAD")
    var mINUPLOAD: String? = ""

    fun getDocDes(): String? {
        return if (LocalizeManager.isThai()) {
            dOCDESCTH
        } else {
            dOCDESCEN
        }
    }
}