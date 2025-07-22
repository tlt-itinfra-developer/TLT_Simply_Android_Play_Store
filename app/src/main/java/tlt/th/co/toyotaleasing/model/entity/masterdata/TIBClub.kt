package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class TIBClub  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("MASTER_ID")
    var mASTERID: String? = ""
    @SerializedName("DES_1")
    var dES1: String? = ""
    @SerializedName("DES_2")
    var dES2: String? = ""
    @SerializedName("AD_NAME")
    var aDNAME: String? = ""
    @SerializedName("AD_IMG")
    var aDIMG: String? = ""
    @SerializedName("AD_INDEX")
    var aDINDEX: String? = ""
    @SerializedName("AD_EXPIRE")
    var aDEXPIRE: String? = ""
    @SerializedName("OPTION")
    var oPTION: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun getDescriptionByLocalize(): String? {
        return if (LocalizeManager.isThai()) {
            dES1
        } else {
            dES2
        }
    }
}