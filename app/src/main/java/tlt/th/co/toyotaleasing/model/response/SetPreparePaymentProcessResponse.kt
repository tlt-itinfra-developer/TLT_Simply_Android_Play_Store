package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class SetPreparePaymentProcessResponse(
        @SerializedName("SEQ_ID") var sEQID: String? = "",
        @SerializedName("ACCOUNT_NO") var aCCOUNTNO: String? = "",
        @SerializedName("REF_CODE1") var rEFCODE1: String? = "",
        @SerializedName("REF_CODE2") var rEFCODE2: String? = ""
)