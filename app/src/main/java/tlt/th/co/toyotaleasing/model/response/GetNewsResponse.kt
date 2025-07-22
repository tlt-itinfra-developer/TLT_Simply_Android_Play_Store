package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class GetNewsResponse(
        @SerializedName("NEWS") var nEWS: List<NEWS?>? = listOf()
) {

    data class NEWS(
            @SerializedName("TYPE_SYNC") var tYPESYNC: String? = "",
            @SerializedName("AD_ID") var aDID: String? = "",
            @SerializedName("HEADER_NEWS") var hEADERNEWS: String? = "",
            @SerializedName("DETAIL_NEWS") var dETAILNEWS: String? = "",
            @SerializedName("HEADER_IMG") var hEADERIMG: String? = "",
            @SerializedName("DETAIL_IMG") var dETAILIMG: String? = "",
            @SerializedName("EXPIRE") var eXPIRE: String? = "",
            @SerializedName("BOOKMARK") var bOOKMARK: String? = "",
            @SerializedName("REC_ACTIVE") var rECACTIVE: String? = "",
            @SerializedName("LINK_PREF") var lINKPREF: String? = ""
    )
}