package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class GetAnnoucementResponse(
        @SerializedName("ANM_ID") var aNM_ID: String? = "",
        @SerializedName("URL_LINK") var uRL_LINK: String? = "",
        @SerializedName("MESSAGE") var mESSAGE: String? = "",
        @SerializedName("LINK_PREF") var linkRef: String? = ""
)