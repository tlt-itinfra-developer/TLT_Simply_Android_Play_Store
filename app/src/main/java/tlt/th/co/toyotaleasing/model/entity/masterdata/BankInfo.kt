package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class BankInfo  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("BM_ID")
    var bMID: String? = ""
    @SerializedName("BANK_CODE")
    var bANKCODE: String? = ""
    @SerializedName("BM_ENABLE")
    var bMENABLE: String? = ""
    @SerializedName("BANK_LINK")
    var bANKLINK: String? = ""
    @SerializedName("BANK_NAME_TH")
    var bANKNAMETH: String? = ""
    @SerializedName("BANK_NAME_EN")
    var bANKNAMEEN: String? = ""
    @SerializedName("BANK_IMAGE")
    var bANKIMAGE: String? = ""
    @SerializedName("FILE_NAME")
    var fILENAME: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""
    @SerializedName("DESCRIPTION")
    var dESCRIPTION: String? = ""
    @SerializedName("CHANNEL")
    var cHANNEL: String? = ""

    fun getBankName() = if (LocalizeManager.isThai()) {
        bANKNAMETH!!
    } else {
        bANKNAMEEN!!
    }
}