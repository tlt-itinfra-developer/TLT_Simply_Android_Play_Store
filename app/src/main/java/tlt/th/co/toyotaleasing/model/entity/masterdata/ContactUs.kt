package tlt.th.co.toyotaleasing.model.entity.masterdata

import com.google.gson.annotations.SerializedName

import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class ContactUs  {
    @SerializedName("TYPE_SYNC")
    var tYPESYNC: String? = ""
    @SerializedName("COMPANY_TH")
    var cOMPANYTH: String? = ""
    @SerializedName("COMPANY_TH1")
    var cOMPANYTH1: String? = ""
    @SerializedName("COMPANY_EN")
    var cOMPANYEN: String? = ""
    @SerializedName("LATITUDE")
    var lATITUDE: String? = ""
    @SerializedName("LONGITUDE")
    var lONGITUDE: String? = ""
    @SerializedName("COMPANY_ADDRESS_TH")
    var cOMPANYADDRESSTH: String? = ""
    @SerializedName("COMPANY_ADDRESS_EN")
    var cOMPANYADDRESSEN: String? = ""
    @SerializedName("SCOPE_OF_RESPONSIBILITY_EN")
    var sCOPEOFRESPONSIBILITYEN: String? = ""
    @SerializedName("SCOPE_OF_RESPONSIBILITY_TH")
    var sCOPEOFRESPONSIBILITYTH: String? = ""
    @SerializedName("CALL_CENTER")
    var cALLCENTER: String? = ""
    @SerializedName("OFFICE_PHONE1")
    var oFFICEPHONE1: String? = ""
    @SerializedName("OFFICE_PHONE2")
    var oFFICEPHONE2: String? = ""
    @SerializedName("OFFICE_PHONE3")
    var oFFICEPHONE3: String? = ""
    @SerializedName("WEBSITE")
    var wEBSITE: String? = ""
    @SerializedName("EMAIL")
    var eMAIL: String? = ""
    @SerializedName("FACEBOOK")
    var fACEBOOK: String? = ""
    @SerializedName("INSTAGRAM")
    var iNSTAGRAM: String? = ""
    @SerializedName("LINE")
    var lINE: String? = ""
    @SerializedName("TWITTER")
    var tWITTER: String? = ""
    @SerializedName("IMG_URL2")
    var iMGURL2: String? = ""
    @SerializedName("IMG_URL3")
    var iMGURL3: String? = ""
    @SerializedName("CONT_DESC")
    var cONTDESC: String? = ""
    @SerializedName("REC_ACTIVE")
    var rECACTIVE: String? = ""

    fun getCompanyName() : String? {
        return if (LocalizeManager.isThai()) {
            cOMPANYTH
        } else {
            cOMPANYEN
        }
    }

    fun getAddress() : String? {
        return if (LocalizeManager.isThai()) {
            cOMPANYADDRESSTH
        } else {
            cOMPANYADDRESSEN
        }
    }

    fun getResponsibility() : String? {
        return if (LocalizeManager.isThai()) {
            sCOPEOFRESPONSIBILITYTH
        } else {
            sCOPEOFRESPONSIBILITYEN
        }
    }
}