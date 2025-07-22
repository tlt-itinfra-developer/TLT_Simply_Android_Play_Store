package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class GetTIBClubDetailResponse(
        @SerializedName("TIB_CLUB_DETAIL") var tIBCLUBDETAIL: List<TIBCLUBDETAIL?>? = listOf()
) {
    data class TIBCLUBDETAIL(
            @SerializedName("TYPE_SYNC") var tYPESYNC: String? = "",
            @SerializedName("TIB_CODE") var tIBCODE: String? = "",
            @SerializedName("HEADER_LINK") var hEADERLINK: String? = "",
            @SerializedName("HEADER_DESC") var hEADERDESC: String? = "",
            @SerializedName("DETAIL_LINK") var dETAILLINK: String? = "",
            @SerializedName("DETAIL_DESC") var dETAILDESC: String? = "",
            @SerializedName("DESCRIPTION") var dESCRIPTION: String? = "",
            @SerializedName("START_TIME") var sTARTTIME: String? = "",
            @SerializedName("END_TIME") var eNDTIME: String? = "",
            @SerializedName("REC_ACTIVE") var rECACTIVE: String? = ""
    )
}