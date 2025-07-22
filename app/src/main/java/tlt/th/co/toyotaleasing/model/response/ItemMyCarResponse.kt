package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName

data class ItemMyCarResponse(
        @SerializedName("RegNumber") var regNumber: String? = "",
        @SerializedName("C_REG_PROVINCE") var cREGPROVINCE: String? = "",
        @SerializedName("ContractNumber") var contractNumber: String? = "",
        @SerializedName("C_NEXT_DUE_DATE") var cNEXTDUEDATE: String? = "",
        @SerializedName("CURRENT_DATE") var cURRENTDATE: String? = "",
        @SerializedName("C_VEHICLE_MODEL") var cVEHICLEMODEL: String? = "",
        @SerializedName("C_SUM_AMT") var cSUMAMT: String? = "",
        @SerializedName("C_INSTALL_DUE_DATE") var cINSTALLDUEDATE: String? = "",
        @SerializedName("C_INSTALL_AMT") var cINSTALLAMT: String? = "",
        @SerializedName("C_PAID_TERM") var cPAIDTERM: String? = "",
        @SerializedName("C_TOTAL_TERM") var cTOTALTERM: String? = "",
        @SerializedName("C_UNPAID_INSTALL") var cUNPAIDINSTALL: String? = "",
        @SerializedName("C_UNPAID_INSTALL_AMT") var cUNPAIDINSTALLAMT: String? = "",
        @SerializedName("C_UNPAID_INSTALL_DUE_DATE") var cUNPAIDINSTALLDUEDATE: String? = "",
        @SerializedName("CAR_IMAGE") var cARIMAGE: String? = "",
        @SerializedName("CAR_DEFAULT") var cARDEFAULT: String? = "",
        @SerializedName("Flag_Pay_Process") var flagPayProcess: String? = "",
        @SerializedName("C_CONTRACT_STATUS") var cCONTRACTSTATUS: String? = "",
        @SerializedName("C_UNPAID_DAY") var cUNPAIDDAY: String? = "",
        @SerializedName("ACCOUNT_NO") var aCCOUNTNO: String? = "",
        @SerializedName("REF_CODE1") var rEFCODE1: String? = "",
        @SerializedName("C_REF_CODE2") var cREFCODE2: String? = "",
        @SerializedName("T_REF_CODE2") var tREFCODE2: String? = "",
        @SerializedName("I_REF_CODE2") var iREFCODE2: String? = "",
        @SerializedName("P_REF_CODE2") var pREFCODE2: String? = "",
        @SerializedName("CONTRACT_STATUS_DESC") var contractDescription: String? = "",
        @SerializedName("Flag_DirectDebit") var flagDirectDebit: String? = ""
)