package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemDataTaxResponse(
        @SerializedName("CURRENT_DATE")
        @Expose
        val cURRENTDATE: String = "",
        @SerializedName("EXT_CONTRACT")
        @Expose
        val eXTCONTRACT: String = "",
        @SerializedName("CONTRACT_NO")
        @Expose
        val cONTRACTNO: String = "",
        @SerializedName("C_CONTRACT_STATUS")
        @Expose
        val cCONTRACTSTATUS: String = "",
        @SerializedName("CUST_NAME")
        @Expose
        val cUSTNAME: String = "",
        @SerializedName("C_VEHICLE_MODEL")
        @Expose
        val cVEHICLEMODEL: String = "",
        @SerializedName("C_VEHICLE_TYPE")
        @Expose
        val cVEHICLETYPE: String = "",
        @SerializedName("C_VEHICLE_GAS")
        @Expose
        val cVEHICLEGAS: String = "",
        @SerializedName("C_VEHICLE_AGE")
        @Expose
        val cVEHICLEAGE: String = "",
        @SerializedName("C_REG_NO")
        @Expose
        val cREGNO: String = "",
        @SerializedName("C_TAX_AMT")
        @Expose
        val cTAXAMT: String = "0",
        @SerializedName("C_REG_PROVINCE")
        @Expose
        val cREGPROVINCE: String = "",
        @SerializedName("C_SERVICE_FEE")
        @Expose
        val cSERVICEFEE: String = "0",
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
        @SerializedName("PROVINCE_CODE")
        @Expose
        val pROVINCECODE: String = "",
        @SerializedName("AMPHUR_CODE")
        @Expose
        val aMPHURCODE: String = "",
        @SerializedName("POST_CODE")
        @Expose
        val pOSTCODE: String = "",
        @SerializedName("C_COMPULSORY_AMT")
        @Expose
        val cCOMPULSORYAMT: String = "0",
        @SerializedName("C_COMPULSORY_AMT1")
        @Expose
        val cCOMPULSORYAMT1: String = "0",
        @SerializedName("C_TAX_PENALTY_AMT")
        @Expose
        val cTAXPENALTYAMT: String = "0",
        @SerializedName("C_TAX_SUM_AMT")
        @Expose
        val taxCSUMAMT: String = "0",
        @SerializedName("C_TAX_EXP_DATE")
        @Expose
        val cTAXEXPDATE: String = "",
        @SerializedName("MOBILE_REGIS")
        @Expose
        val mOBILEREGIS: String = "",
        @SerializedName("EMAIL_VERI")
        @Expose
        val eMAILVERI: String = "",
        @SerializedName("MOBILE_NO")
        @Expose
        val mobileNo: String = "",
        @SerializedName("EMAIL")
        @Expose
        val email: String = ""
)