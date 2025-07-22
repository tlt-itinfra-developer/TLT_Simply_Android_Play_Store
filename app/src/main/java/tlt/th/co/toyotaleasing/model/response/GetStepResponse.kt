package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetStepResponse(
        @SerializedName("STATUS")
        val status: String = "",
        @SerializedName("REF_ID")
        val ref_id: String = "",
        @SerializedName("REF_URL")
        val ref_url: String = ""
)