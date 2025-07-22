package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class PayCode  {
    @SerializedName("TYPE_SYNC") var tYPESYNC: String? = ""
    @SerializedName("ID") var iD: String? = ""
    @SerializedName("PAY_CODE") var pAYCODE: String? = ""
    @SerializedName("PAY_NAME_TH") var pAYNAMETH: String? = ""
    @SerializedName("PAY_NAME_EN") var pAYNAMEEN: String? = ""
    @SerializedName("DP_TYPE") var dPTYPE: String? = ""
    @SerializedName("BP_TYPE") var bPTYPE: String? = ""
    @SerializedName("REC_ACTIVE") var rECACTIVE: String? = ""

    fun getPayName() = if (LocalizeManager.isThai()) {
        pAYNAMETH
    } else {
        pAYNAMEEN
    }
}