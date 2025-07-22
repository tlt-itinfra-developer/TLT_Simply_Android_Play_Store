package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class ItemCarsLoanResponse {
    @SerializedName("CAR_IMAGE")
    var cARIMAGE: String? = ""
    @SerializedName("CAR_MODEL")
    var cARMODEL: String? = ""
    @SerializedName("CAR_PRICE")
    var cARPRICE: String? = ""
    @SerializedName("CAR_GRADE")
    var cARGRADE: String? = ""
    @SerializedName("EXPIRE_DATE")
    var eXPIREDATE: String? = ""
    @SerializedName("REF_ID")
    var rEFID: String? = ""
    @SerializedName("REF_STATUS")
    var rEFSTATUS: String? = ""
    @SerializedName("REF_DES_TH")
    var rEFDESTH: String? = ""
    @SerializedName("REF_DES_EN")
    var rEFDESEN: String? = ""
    @SerializedName("STAMP_DATE")
    var sTAMPDATE: String? = ""
    
    fun getResDes() = if (LocalizeManager.isThai()) {
        rEFDESTH
    } else {
        rEFDESEN
    }
}

