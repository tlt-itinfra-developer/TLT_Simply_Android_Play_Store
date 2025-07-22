package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class TIBQuestion  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("ASK_ID")
    var aSKID: String? = ""
    @SerializedName("TIB_QUESTION_TH")
    var tIBQUESTIONTH: String? = ""
    @SerializedName("TIB_QUESTION_EN")
    var tIBQUESTIONEN: String? = ""
    @SerializedName("TIB_DESCRIPTION")
    var tIBDESCRIPTION: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun tIBQUESTION(): String? {
        return if (LocalizeManager.isThai()) {
            tIBQUESTIONTH
        } else {
            tIBQUESTIONEN
        }
    }
}