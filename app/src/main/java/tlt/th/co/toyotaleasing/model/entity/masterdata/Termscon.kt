package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName


open class Termscon  {
    @SerializedName("TYPE_SYNC") var tYPESYNC: String? = ""
    @SerializedName("MASTER_ID") var mASTERID: String? = ""
    @SerializedName("TERM_INFO_EN") var tERMINFOEN: String? = ""
    @SerializedName("TERM_INFO_TH") var tERMINFOTH: String? = ""
    @SerializedName("TERM_FUNC1") var tERMFUNC1: String? = ""
    @SerializedName("TERM_FUNC2") var tERMFUNC2: String? = ""
    @SerializedName("TERM_FUNC3") var tERMFUNC3: String? = ""
    @SerializedName("TERM_FUNC4") var tERMFUNC4: String? = ""
    @SerializedName("REC_ACTIVE") var rECACTIVE: String? = ""
}