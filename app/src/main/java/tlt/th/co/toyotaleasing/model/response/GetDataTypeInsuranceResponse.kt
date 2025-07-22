package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

data class GetDataTypeInsuranceResponse(
        @SerializedName("history") var history: List<Item?>? = listOf(),
        @SerializedName("recommend") var recommend: List<Item?>? = listOf(),
        @SerializedName("status") var status: Status?
) {
    data class Item(
            @SerializedName("EXT_CONTRACT") var eXTCONTRACT: String? = "",
            @SerializedName("POLICY_NO") var pOLICYNO: String? = "",
            @SerializedName("I_POLICY_NO") var iPOLICYNO: String? = "",
            @SerializedName("INS_COM_NAME_TH") var iNSCOMNAMETH: String? = "",
            @SerializedName("INS_COM_NAME_EN") var iNSCOMNAMEEN: String? = "",
            @SerializedName("COVERATE_TYPE") var cOVERATETYPE: String? = "",
            @SerializedName("COVERAGE_AMT") var cOVERAGEAMT: String? = "",
            @SerializedName("REPAIR_CON") var rEPAIRCON: String? = "",
            @SerializedName("EXP_DATE") var eXPDATE: String? = "",
            @SerializedName("INS_COM_LOGO") var iNSCOMLOGO: String? = "",
            @SerializedName("INS_COMPULSORY_AMT") var iNSCOMPULSORYAMT: String? = "",
            @SerializedName("INS_SUM_AMT") var iNSSUMAMT: String? = "",
            @SerializedName("INS_PREMIUM_AMT") var iNSPREMIUMAMT: String? = ""
    ) {
        val iNSCOMNAME: String
            get() {
                return if (LocalizeManager.isThai()) {
                    iNSCOMNAMETH!!
                } else {
                    iNSCOMNAMEEN!!
                }
            }
    }

    data class Status(
            @SerializedName("EXT_CONTRACT") var eXTCONTRACT: String? = "",
            @SerializedName("POST_DATE") var pOSTDATE: String? = "",
            @SerializedName("TRACKING_NO") var tRACKINGNO: String? = ""
    )
}