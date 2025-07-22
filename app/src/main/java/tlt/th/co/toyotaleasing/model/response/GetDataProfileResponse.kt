package tlt.th.co.toyotaleasing.model.response

import com.google.gson.annotations.SerializedName


data class GetDataProfileResponse(
        @SerializedName("CUST_NAME") var cUSTNAME: String? = "",
        @SerializedName("EMAIL") var eMAIL: String? = "",
        @SerializedName("PHONE") var pHONE: String? = "",
        @SerializedName("FLAG_TIB") var fLAGTIB: String? = "",
        @SerializedName("FLAG_TLT") var fLAGTLT: String? = "",
        @SerializedName("flag_not_regis") var flagNotRegis: String? = "",
        @SerializedName("flag_not_ver_email") var flagNotVerEmail: String? = "",
        @SerializedName("flag_not_ver_email1") var flagNotVerEmail1: String? = "",
        @SerializedName("flag_not_in_contract") var flagNotInContract: String? = "",
        @SerializedName("INSTALLMENT_CONFIG") var iNSTALLMENTCONFIG: String? = "",
        @SerializedName("INSURANCE_CONFIG") var iNSURANCECONFIG: String? = "",
        @SerializedName("TAX_CONFIG") var tAXCONFIG: String? = "",
        @SerializedName("FLAG_NEWS") var fLAGNEWS: String? = "",
        @SerializedName("FLAG_TIBCLUB") var fLAGTIBCLUB: String? = "",
        @SerializedName("LANGUAGE") var lANGUAGE: String? = "",
        @SerializedName("PRO_IMAGE") var pROIMAGE: String? = "",
        @SerializedName("CHB_ID") var chatbotId: String? = "",
        @SerializedName("flag_adhocmenu") var fLAGADHOCMENU: List<adhocmenu?>? = listOf()
){

    data class adhocmenu(
            @SerializedName("MENU_ID") var mENUID: String? = "" ,
            @SerializedName("STATUS") var sTATUS: String? = ""
    )
}