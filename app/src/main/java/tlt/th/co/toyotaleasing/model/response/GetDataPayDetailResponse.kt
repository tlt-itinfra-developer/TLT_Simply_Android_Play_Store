package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetDataPayDetailResponse(
        @SerializedName("C_NAME") var cNAME: String? = "",
        @SerializedName("AMOUNT") var aMOUNT: Double? = 0.0,
        @SerializedName("BP_TYPE") var bPTYPE: String? = "",
        @SerializedName("BP_CODE") var bPCODE: String? = ""
)