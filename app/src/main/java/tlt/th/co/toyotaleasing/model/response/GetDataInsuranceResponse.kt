package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

data class GetDataInsuranceResponse(
        @SerializedName("EXT_CONTRACT") var eXTCONTRACT: String? = "",
        @SerializedName("CUST_NAME") var cUSTNAME: String? = "",
        @SerializedName("C_REG_NO") var cREGNO: String? = "",
        @SerializedName("C_REG_PROVINCE") var cREGPROVINCE: String? = "",
        @SerializedName("CURRENT_DATE") var cURRENTDATE: String? = "",
        @SerializedName("I_REF_CODE1") var iREFCODE1: String? = "",
        @SerializedName("I_REF_CODE2") var iREFCODE2: String? = "",
        @SerializedName("I_POLICY_NO") var iPOLICYNO: String? = "",
        @SerializedName("I_TYPE_COVERAGE") var iTYPECOVERAGE: String? = "",
        @SerializedName("I_COVERAGE_AMT") var iCOVERAGEAMT: String? = "",
        @SerializedName("I_EXP_DATE") var iEXPDATE: String? = "",
        @SerializedName("I_EXP_DAY") var iEXPDAY: String? = "",
        @SerializedName("I_REPAIR_CON") var iREPAIRCON: String? = "",
        @SerializedName("I_SUM_AMT") var iSUMAMT: String? = "",
        @SerializedName("INS_COM_NAME_TH") var iNSCOMNAMETH: String? = "",
        @SerializedName("INS_COM_NAME_EN") var iNSCOMNAMEEN: String? = "",
        @SerializedName("Flag_INS_PROCESS") var flagINSPROCESS: String? = "",
        @SerializedName("FLAG_SETTLEMENT") var flagSETTLEMENT: String? = "",
        @SerializedName("Flag_PROHIBIT") var flagPROHIBIT: String? = "",
        @SerializedName("Flag_4M") var flag4M: String? = ""
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