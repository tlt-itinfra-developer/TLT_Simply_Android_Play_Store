package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class ItemInstallmentResponse{
        @SerializedName("EXT_CONTRACT")
        @Expose
        val eXTCONTRACT: String = ""
        @SerializedName("CONTRACT_NO")
        @Expose
        val cONTRACTNO: String = ""
        @SerializedName("C_CONTRACT_STATUS")
        @Expose
        val cCONTRACTSTATUS: String = ""
        @SerializedName("CUST_NAME")
        @Expose
        val cUSTNAME: String = ""
        @SerializedName("C_VEHICLE_MODEL")
        @Expose
        val cVEHICLEMODEL: String = ""
        @SerializedName("C_REG_NO")
        @Expose
        val cREGNO: String = ""
        @SerializedName("C_REG_PROVINCE")
        @Expose
        val cREGPROVINCE: String = ""
        @SerializedName("C_CONTRACT_DATE")
        @Expose
        val cCONTRACTDATE: String = ""
        @SerializedName("CURRENT_DATE")
        @Expose
        val cURRENTDATE: String = ""
        @SerializedName("C_INSTALL_DUE_DATE")
        @Expose
        val cINSTALLDUEDATE: String = ""
        @SerializedName("C_NEXT_DUE_DATE")
        @Expose
        val cNEXTDUEDATE: String = ""
        @SerializedName("C_INSTALL_AMT")
        @Expose
        val cINSTALLAMT: String = ""
        @SerializedName("C_SUM_AMT")
        @Expose
        val cSUMAMT: String = ""
        @SerializedName("C_PAID_TERM")
        @Expose
        val cPAIDTERM: String = ""
        @SerializedName("C_TOTAL_TERM")
        @Expose
        val cTOTALTERM: String = ""
        @SerializedName("C_UNPAID_INSTALL")
        @Expose
        val cUNPAIDINSTALL: String = ""
        @SerializedName("C_UNPAID_INSTALL_AMT")
        @Expose
        val cUNPAIDINSTALLAMT: String = ""
        @SerializedName("C_TOTAL_PAID_AMT")
        @Expose
        val cTOTALPAIDAMT: String = ""
        @SerializedName("C_OUT_STANDING_AMT")
        @Expose
        val cOUTSTANDINGAMT: String = ""
        @SerializedName("C_PENALTY")
        @Expose
        val cPENALTY: String = ""
        @SerializedName("C_TUCP_CAR_PRICE")
        @Expose
        val cTUCPCARPRICE: String = ""
        @SerializedName("EMAIL_VERI")
        @Expose
        val eMAILVERI: String = ""
        @SerializedName("ADDRESS_LINE1")
        @Expose
        val aDDRESSLINE1: String = ""
        @SerializedName("ADDRESS_LINE2")
        @Expose
        val aDDRESSLINE2: String = ""
        @SerializedName("ADDRESS_LINE3")
        @Expose
        val aDDRESSLINE3: String = ""
        @SerializedName("ADDRESS_LINE4")
        @Expose
        val aDDRESSLINE4: String = ""
        @SerializedName("PROVINCE_CODE")
        @Expose
        val pROVINCECODE: String = ""
        @SerializedName("AMPHUR_CODE")
        @Expose
        val aMPHURCODE: String = ""
        @SerializedName("POST_CODE")
        @Expose
        val pOSTCODE: String = ""
        @SerializedName("EMAIL")
        @Expose
        val eMAIL: String = ""
        @SerializedName("MOBILE_NO")
        @Expose
        val mOBILENO: String = ""
        @SerializedName("MOBILE_REGIS")
        @Expose
        val mOBILEREGIS: String = ""
        @SerializedName("DEALER_CODE")
        @Expose
        val dEALERCODE: String = ""
        @SerializedName("SHOWROOM_CODE")
        @Expose
        val sHOWROOMCODE: String = ""
        @SerializedName("DEALERNAME_EN")
        @Expose
        val dEALERNAMEEN: String = ""
        @SerializedName("DEALERNAME_TH")
        @Expose
        val dEALERNAMETH: String = ""
        @SerializedName("DEALER_IMAGE1")
        @Expose
        val dEALERIMAGE1: String = ""
        @SerializedName("WEBSITE")
        @Expose
        val wEBSITE: String = ""
        @SerializedName("C_TOTAL_PAYOFF_AMT")
        @Expose
        val cTOTALPAYOFFAMT: String = ""
        @SerializedName("Flag_PayOff")
        @Expose
        val flagPayOff: String = "N"
        @SerializedName("C_REFINANCE_AMT")
        @Expose
        val cREFINANCEAMT: String = ""
        @SerializedName("Flag_Refinance")
        @Expose
        val flagRefinance: String = "N"
        @SerializedName("Flag_Payoff_Process")
        @Expose
        val flagPayoffProcess: String = "N"
        @SerializedName("C_PAYMENT_CHANNEL")
        @Expose
        val paymentMethod: String = ""
        @SerializedName("C_BANK_NAME")
        @Expose
        val bankName: String = ""
        @SerializedName("C_BANK_ACC_NAME")
        @Expose
        val bankAccName: String = ""
        @SerializedName("C_BANK_ACC_NO")
        @Expose
        val bankAccNo: String = ""
        @SerializedName("CONTRACT_STATUS_DESC")
        @Expose
        val contractDescription: String = ""
        @SerializedName("DEALER_NOSTRA_ID")
        @Expose
        val dealerNostraId: String = ""
        @SerializedName("FOLLOWUP_AMOUNT")
        @Expose
        val followupfee: String = ""
        @SerializedName("PAYMENT_DUE_DATE_TH")
        @Expose
        val paymentduedateTH: String = ""
        @SerializedName("PAYMENT_DUE_DATE_EN")
        @Expose
        val paymentduedateEN: String = ""

        fun paymentduedate(): String {
                return if (LocalizeManager.isThai()) {
                        paymentduedateTH
                } else {
                        paymentduedateEN
                }
        }
}

