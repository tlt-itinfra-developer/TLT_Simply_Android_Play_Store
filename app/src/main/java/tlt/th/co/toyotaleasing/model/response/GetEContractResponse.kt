package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName

data class GetEContractResponse(
    @SerializedName("PDF_FILE")
    var pDFFILE: String,
    @SerializedName("PDF_NAME")
    var pDFNAME: String ,
    @SerializedName("TYPE")
    var tYPE: String,
    @SerializedName("FLAG_INTDOC")
    var fLAGINTDOC: String
)