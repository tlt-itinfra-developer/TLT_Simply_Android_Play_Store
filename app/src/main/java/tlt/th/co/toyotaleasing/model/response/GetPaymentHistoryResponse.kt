package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class GetPaymentHistoryResponse(
        @SerializedName("PAYMENT_HIS") var pAYMENTHIS: List<PAYMENTHIS?>? = listOf()
) {
    data class PAYMENTHIS(
            @SerializedName("DOC_ID") var rECEIPTID: String? = "",
            @SerializedName("RECEIPT_DTE") var receiptDate: String? = "",
            @SerializedName("RECEIVE_DTE") var receiveDate: String? = "",
            @SerializedName("RECEIPT_AMT") var rECEIPTAMT: String? = "",
            @SerializedName("DOC_NO") var dOCNBR: String? = "",
            @SerializedName("RECEIPT_STS_CODE") var rECEIPTSTSCODE: String? = "",
            @SerializedName("BANK_NME") var bANKNME: String? = "",
            @SerializedName("CANCELED_DTE") var cANCELEDDTE: String? = "",
            @SerializedName("EXT_CONTRACT") var eXTCONTRACT: String? = ""
    )
}