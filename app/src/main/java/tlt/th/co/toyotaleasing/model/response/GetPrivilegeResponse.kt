package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetPrivilegeResponse(
        @SerializedName("Previlleges") var privileges: List<Privilege>? = listOf()
) {
    data class Privilege(
            @SerializedName("AD_ID") var aDID: String? = "",
            @SerializedName("AD_TYPE") var aDTYPE: String? = "",
            @SerializedName("AD_SUBJECT") var aDSUBJECT: String? = "",
            @SerializedName("CONTENT_DETAIL") var cONTENTDETAIL: String? = "",
            @SerializedName("CONTENT_IMG1") var cONTENTIMG1: String? = "",
            @SerializedName("CONTENT_IMG2") var cONTENTIMG2: String? = "",
            @SerializedName("ENABLE_BTN") var eNABLEBTN: String? = "",
            @SerializedName("EXPIRE_DATE") var eXPIREDATE: String? = "",
            @SerializedName("REF_PRIVCODE") var rEFPRIVCODE: String? = "",
            @SerializedName("START_DATE") var sTARTDATE: String? = ""
    )
}