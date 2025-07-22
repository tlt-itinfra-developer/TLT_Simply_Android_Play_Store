package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class PayMethod  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("ID")
    var iD: String? = ""
    @SerializedName("DATA_TYPE")
    var dATATYPE: String? = ""
    @SerializedName("TEXT_TH")
    var tEXTTH: String? = ""
    @SerializedName("TEXT_EN")
    var tEXTEN: String? = ""
    @SerializedName("TEXT_INDEX")
    var tEXTINDEX: String? = ""
    @SerializedName("DATA_DESC")
    var dATADESC: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    val text: String
        get() = if (LocalizeManager.isThai()) {
            tEXTTH
        } else {
            tEXTEN
        } ?: ""
}