package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName

data class FillInfoResponse(
    @SerializedName("AMPHUR")
    var aMPHUR: String,
    @SerializedName("AMPHUR_CODE")
    var aMPHURCODE: String,
    @SerializedName("CITIZEN_ID")
    var cITIZENID: String,
    @SerializedName("DOB")
    var dOB: String,
    @SerializedName("SEX")
    var sEX: String,
    @SerializedName("NAME_TH")
    var nAMETH: String,
    @SerializedName("POSTCODE")
    var pOSTCODE: String,
    @SerializedName("PROVINCE")
    var pROVINCE: String,
    @SerializedName("PROVINCE_CODE")
    var pROVINCECODE: String,
    @SerializedName("REAL_ADDRESS")
    var rEALADDRESS: String,
    @SerializedName("REF_ID")
    var rEFID: String,
    @SerializedName("SURNAME_TH")
    var sURNAMETH: String ,
    @SerializedName("HOUSEHOLD_ID")
    var hOUSEHOLDID: String
)