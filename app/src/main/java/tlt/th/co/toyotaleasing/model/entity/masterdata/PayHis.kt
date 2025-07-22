package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName



open class PayHis  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("PAYHIS_ID")
    var pAYHISID: String? = ""
    @SerializedName("PAYHIS_DES1")
    var pAYHISDES1: String? = ""
    @SerializedName("PAYHIS_DES2")
    var pAYHISDES2: String? = ""
    @SerializedName("MASTER_DESC")
    var mASTERDESC: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""
}