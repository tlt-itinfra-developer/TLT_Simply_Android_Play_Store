package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import tlt.th.co.toyotaleasing.manager.LocalizeManager

open class PersonalResponse{
    @SerializedName("AMPHUR")
    @Expose
    var aMPHUR: String? = ""
    @SerializedName("AMPHUR_CODE")
    @Expose
    var aMPHURCODE: String? = ""
    @SerializedName("ASN_01")
    @Expose
    var aSN01: String? = ""
    @SerializedName("ASN_02")
    @Expose
    var aSN02: String? = ""
    @SerializedName("CITIZEN_ID")
    @Expose
    var cITIZENID: String? = ""
    @SerializedName("CUST_NAME")
    @Expose
    var cUSTNAME: String? = ""
    @SerializedName("DISTRICT")
    @Expose
    var dISTRICT: String? = ""
    @SerializedName("DISTRICT_CODE")
    @Expose
    var dISTRICTCODE: String? = ""
    @SerializedName("DOB")
    @Expose
    var dOB: String? = ""
    @SerializedName("EMP_MONTH")
    @Expose
    var eMPMONTH: String? = ""
    @SerializedName("EMP_YEAR")
    @Expose
    var eMPYEAR: String? = ""
    @SerializedName("IF_YES")
    @Expose
    var iFYES: String? = ""
    @SerializedName("INCOME")
    @Expose
    var iNCOME: String? = ""
    @SerializedName("LIVING_MONTH")
    @Expose
    var lIVINGMONTH: String? = ""
    @SerializedName("LIVING_YEAR")
    @Expose
    var lIVINGYEAR: String? = ""
    @SerializedName("MARITAL_ID")
    @Expose
    var mARITALID: String? = ""
    @SerializedName("MARITAL_STATUS")
    @Expose
    var mARITALSTATUS: String? = ""
    @SerializedName("O_INCOME")
    @Expose
    var oINCOME: String? = ""
    @SerializedName("POSTCODE")
    @Expose
    var pOSTCODE: String? = ""
    @SerializedName("PROPERTY_ID")
    @Expose
    var pROPERTYID: String? = ""
    @SerializedName("PROPERTY_TYPE")
    @Expose
    var pROPERTYTYPE: String? = ""
    @SerializedName("PROVINCE")
    @Expose
    var pROVINCE: String? = ""
    @SerializedName("PROVINCE_CODE")
    @Expose
    var pROVINCECODE: String? = ""
    @SerializedName("REAL_ADDRESS")
    @Expose
    var rEALADDRESS: String? = ""
    @SerializedName("SUB_OCCUPATION")
    @Expose
    var sUBOCCUPATION: String? = ""
    @SerializedName("SUB_OCCUPATION_ID")
    @Expose
    var sUBOCCUPATIONID:String? = ""
    @SerializedName("OCCUPATION_ID")
    @Expose
    var oCCUPATIONID:String? = ""
    @SerializedName("OCCUPATION_EN")
    @Expose
    var oCCUPATIONEN:String? = ""
    @SerializedName("OCCUPATION_TH")
    @Expose
    var oCCUPATIONTH:String? = ""
    @SerializedName("EMPLOYMENT_TYPE_DESC_TH")
    @Expose
    var eMPLOYMENTTYPEDESCTH:String? = ""
    @SerializedName("EMPLOYMENT_TYPE_DESC_EN")
    @Expose
    var eMPLOYMENTTYPEDESCEN:String? = ""
    @SerializedName("PLAT")
    @Expose
    var pLat:String? = ""
    @SerializedName("PLONG")
    @Expose
    var pLong:String? = ""
    @SerializedName("COMPANY_NAME")
    @Expose
    var cOMPANYNAME:String? = ""

    fun eMPLOYMENTTYPEDESC(): String? {
        return if (LocalizeManager.isThai()) {
            eMPLOYMENTTYPEDESCTH
        } else {
            eMPLOYMENTTYPEDESCEN
        }
    }

    fun oCCUPATION(): String? {
        return if (LocalizeManager.isThai()) {
            oCCUPATIONTH
        } else {
            oCCUPATIONEN
        }
    }


}



