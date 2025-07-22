package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class GetReceiptDocumentResponse(
        @SerializedName("DOC_NO") var dOCNO: String? = "",
        @SerializedName("DOC_DATE") var dOCDATE: String? = "",
        @SerializedName("EXT_CONTRACT") var eXTCONTRACT: String? = "",
        @SerializedName("DATA") var dATA: String? = ""
)