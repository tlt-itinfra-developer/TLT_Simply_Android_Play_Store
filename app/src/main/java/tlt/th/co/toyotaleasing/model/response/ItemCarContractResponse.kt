package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemCarContractResponse(
        @SerializedName("EXT_CONTRACT")
        @Expose
        val eXTCONTRACT: String = "",
        @SerializedName("CONTRACT_NO")
        @Expose
        val cONTRACTNO: String = "",
        @SerializedName("C_CUST_STATUS")
        @Expose
        val cCUSTSTATUS: String = "",
        @SerializedName("CUST_NAME")
        @Expose
        val cUSTNAME: String = "",
        @SerializedName("C_VEHICLE_MODEL")
        @Expose
        val cVEHICLEMODEL: String = "",
        @SerializedName("C_REG_NO")
        @Expose
        val cREGNO: String = "",
        @SerializedName("C_REG_PROVINCE")
        @Expose
        val cREGPROVINCE: String = "",
        @SerializedName("C_CONTRACT_DATE")
        @Expose
        val cCONTRACTDATE: String = "",
        @SerializedName("C_INSTALL_DUE_DATE")
        @Expose
        val cINSTALLDUEDATE: String = "",
        @SerializedName("C_INSTALL_AMT")
        @Expose
        val cINSTALLAMT: String = "",
        @SerializedName("C_PAID_TERM")
        @Expose
        val cPAIDTERM: String = "",
        @SerializedName("C_TOTAL_TERM")
        @Expose
        val cTOTALTERM: String = "",
        @SerializedName("C_UNPAID_INSTALL_AMT")
        @Expose
        val cUNPAIDINSTALLAMT: String = "",
        @SerializedName("C_TOTAL_PAID_AMT")
        @Expose
        val cTOTALPAIDAMT: String = "",
        @SerializedName("C_OUT_STANDING_AMT")
        @Expose
        val cOUTSTANDINGAMT: String = "",
        @SerializedName("C_PENALTY")
        @Expose
        val cPENALTY: String = "",
        @SerializedName("C_TUCP_CAR_PRICE")
        @Expose
        val cTUCPCARPRICE: String = "",
        @SerializedName("ADDRESS_LINE1")
        @Expose
        val aDDRESSLINE1: String = "",
        @SerializedName("ADDRESS_LINE2")
        @Expose
        val aDDRESSLINE2: String = "",
        @SerializedName("ADDRESS_LINE3")
        @Expose
        val aDDRESSLINE3: String = "",
        @SerializedName("ADDRESS_LINE4")
        @Expose
        val aDDRESSLINE4: String = "",
        @SerializedName("POST_CODE")
        @Expose
        val pOSTCODE: String = "",
        @SerializedName("DEALER_CODE")
        @Expose
        val dEALERCODE: String = "",
        @SerializedName("SHOWROOM_CODE")
        @Expose
        val sHOWROOMCODE: String = "",
        @SerializedName("DEALERNAME_EN")
        @Expose
        val dEALERNAMEEN: String = "",
        @SerializedName("DEALERNAME_TH")
        @Expose
        val dEALERNAMETH: String = "",
        @SerializedName("DEALER_IMAGE1")
        @Expose
        val dEALERIMAGE1: String = "",
        @SerializedName("WEBSITE")
        @Expose
        val wEBSITE: String = ""
)