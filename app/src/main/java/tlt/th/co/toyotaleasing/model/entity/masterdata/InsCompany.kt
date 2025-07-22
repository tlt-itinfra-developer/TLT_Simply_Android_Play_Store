package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class InsCompany  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("INS_COM_CODE")
    var iNSCOMCODE: String? = ""
    @SerializedName("INS_COM_SHORTNAME_TH")
    var iNSCOMSHORTNAMETH: String? = ""
    @SerializedName("INS_COM_SHORTNAME_EN")
    var iNSCOMSHORTNAMEEN: String? = ""
    @SerializedName("INS_COM_NAME_TH")
    var iNSCOMNAMETH: String? = ""
    @SerializedName("INS_COM_NAME_EN")
    var iNSCOMNAMEEN: String? = ""
    @SerializedName("INS_COM_PHONE")
    var iNSCOMPHONE: String? = ""
    @SerializedName("INS_ADDRESS_TH")
    var iNSADDRESSTH: String? = ""
    @SerializedName("INS_COM_LOGO")
    var iNSCOMLOGO: String? = ""
    @SerializedName("INS_ADDRESS_EN")
    var iNSADDRESSEN: String? = ""
    @SerializedName("INS_COM_WEBSITE")
    var iNSCOMWEBSITE: String? = ""
    @SerializedName("INS_COM_DESCRIPTION")
    var iNSCOMDESCRIPTION: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun iNSCOMNAME() : String? {
        return if (LocalizeManager.isThai()) {
            iNSCOMNAMETH
        } else {
            iNSCOMNAMEEN
        }
    }

    fun iNSCOMSHORTNAME() : String? {
        return if (LocalizeManager.isThai()) {
            iNSCOMSHORTNAMETH
        } else {
            iNSCOMSHORTNAMEEN
        }
    }
}