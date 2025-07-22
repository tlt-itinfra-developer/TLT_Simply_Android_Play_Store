package tlt.th.co.toyotaleasing.model.response


import com.google.gson.annotations.SerializedName

data class ItemDocUpload(
    @SerializedName("DOC_IMG")
    var dOCIMG: String,
    @SerializedName("DOC_NAME")
    var dOCNAME: String,
    @SerializedName("DOC_TYPE")
    var dOCTYPE: String
)