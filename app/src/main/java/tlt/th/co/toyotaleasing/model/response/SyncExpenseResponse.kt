package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class SyncExpenseResponse(
        @SerializedName("doc_name")
        val doc_name: String = "",
        @SerializedName("doc_pdf")
        val doc_base64: String = ""
)

